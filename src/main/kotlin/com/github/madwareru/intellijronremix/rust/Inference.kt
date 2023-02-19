package com.github.madwareru.intellijronremix.rust

import com.github.madwareru.intellijronremix.language.psi.RONFieldName
import com.github.madwareru.intellijronremix.language.psi.RONFile
import com.github.madwareru.intellijronremix.language.psi.RONList
import com.github.madwareru.intellijronremix.language.psi.RONMap
import com.github.madwareru.intellijronremix.language.psi.RONObject
import com.github.madwareru.intellijronremix.language.psi.RONObjectName
import com.github.madwareru.intellijronremix.language.psi.RONOption
import com.github.madwareru.intellijronremix.language.psi.RONTuple
import com.github.madwareru.intellijronremix.language.psi.RONValue
import com.github.madwareru.intellijronremix.language.psi.childOfType
import com.intellij.injected.editor.VirtualFileWindow
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.*
import org.rust.lang.core.psi.RsEnumItem
import org.rust.lang.core.psi.RsEnumVariant
import org.rust.lang.core.psi.RsModItem
import org.rust.lang.core.psi.RsNamedFieldDecl
import org.rust.lang.core.psi.RsStructItem
import org.rust.lang.core.psi.ext.*
import org.rust.lang.core.psi.rustStructureModificationTracker
import org.rust.lang.core.resolve.indexes.RsTypeAliasIndex
import org.rust.lang.core.resolve.knownItems
import org.rust.lang.core.stubs.index.RsNamedElementIndex
import org.rust.lang.core.types.Substitution
import org.rust.lang.core.types.TyFingerprint
import org.rust.lang.core.types.infer.substitute
import org.rust.lang.core.types.normType
import org.rust.lang.core.types.rawType
import org.rust.lang.core.types.ty.TyReference as RsTypeRef
import org.rust.lang.core.types.ty.TyTuple as RsTypeTuple
import org.rust.lang.core.types.ty.TySlice as RsTypeSlice
import org.rust.openapiext.getElements
import org.rust.lang.core.types.ty.Ty as RsType
import org.rust.lang.core.types.ty.TyAdt as RsTypeAdt
import org.rust.lang.core.types.ty.TyArray as RsTypeArray

@Suppress("PrivatePropertyName")
private val INFERENCE_KEY: Key<CachedValue<InferenceResult>> = Key.create("RON_TO_RUST_INFERENCE_KEY")

val PsiElement.ronToRustInferenceContext: InferenceResult
    get() {
        val contextParent = this.containingFile as RONFile
        return contextParent.ronToRustInferenceContext
    }

val RONObjectName.inference: TypeInferenceResult get() = ronToRustInferenceContext.objects.getValue(this)
val RONFieldName.inference: FieldInferenceResult get() = ronToRustInferenceContext.fields.getValue(this)

val RONFile.ronToRustInferenceContext: InferenceResult
    get() =
        CachedValuesManager.getManager(project).getCachedValue(this, INFERENCE_KEY, {
            val builder = InferenceBuilder()
            builder.infer(this)
            val result: InferenceResult = builder.finish()
            if (containingFile.virtualFile is VirtualFileWindow) {
                CachedValueProvider.Result.create(result, PsiModificationTracker.MODIFICATION_COUNT)
            } else {
                CachedValueProvider.Result.create(
                    result,
                    listOf(this.modificationTracker, project.rustStructureModificationTracker)
                )
            }
        }, false)

data class FieldInferenceResult(val possibleFields: List<RsNamedFieldDecl>)
data class TypeInferenceResult(
    val possibleDeclarations: List<RsNamedElement>,
    /**
     * Variants for completion. Should only contain completion options with matching type.
     * Other completion options can be calculated elsewhere, because they don't need knowledge from inference.
     */
    val variants: Array<RsNamedElement>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TypeInferenceResult

        if (possibleDeclarations != other.possibleDeclarations) return false
        if (!variants.contentEquals(other.variants)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = possibleDeclarations.hashCode()
        result = 31 * result + variants.contentHashCode()
        return result
    }
}

class InferenceResult(
    val fields: Map<RONFieldName, FieldInferenceResult>,
    val objects: Map<RONObjectName, TypeInferenceResult>,
)

private class InferenceBuilder(
    val fields: MutableMap<RONFieldName, FieldInferenceResult> = mutableMapOf(),
    val objects: MutableMap<RONObjectName, TypeInferenceResult> = mutableMapOf(),
) {
    fun finish(): InferenceResult {
        return InferenceResult(fields, objects)
    }

    fun infer(file: RONFile) {
        val value = file.childOfType<RONValue>() ?: return
        infer(value, emptySet())
    }

    /**
     * While reference types are not deserializable, they can be serialized,
     * so we should try to parse them as well.
     * And Box types can be deserialized.
     */
    private fun RsType.deref(): RsType {
        return when (this) {
            is RsTypeRef -> this.referenced.substitute(this.typeParameterValues).deref()
            is RsTypeAdt -> {
                if (this.item == this.item.knownItems.Box) {
                    this.typeArguments.first().substitute(this.typeParameterValues).deref()
                } else {
                    this
                }
            }
            else -> this
        }
    }

    private fun Iterable<RsType>.deref(): List<RsType> {
        return map { it.deref() }
    }

    private fun infer(value: RONValue, possibleTypes: Set<RsType>) {
        val adaptedTypes = possibleTypes.deref().flatMap { it.unwrapNewTypes() }.toSet()
        when (val child = value.children.singleOrNull()) {
            is RONOption -> infer(child, adaptedTypes)
            is RONList -> infer(child, adaptedTypes)
            is RONMap -> infer(child, adaptedTypes)
            is RONObject -> infer(child, adaptedTypes)
            is RONTuple -> infer(child, adaptedTypes)
            is RONObjectName -> infer(child, adaptedTypes)
        }
    }

    private fun infer(option: RONOption, possibleTypes: Set<RsType>) {
        val someBody = option.value ?: return
        val possibleInnerTypes = possibleTypes.mapNotNull {
            val adt = it as? RsTypeAdt ?: return@mapNotNull null
            if (adt.item != adt.item.knownItems.Option) return@mapNotNull null
            val innerType = adt.typeArguments.single()
            innerType.substitute(adt.typeParameterValues)
        }.toSet()
        infer(someBody, possibleInnerTypes)
    }

    private fun infer(list: RONList, possibleTypes: Set<RsType>) {
        val values = list.valueList
        // We just assume, that the inner type is the first type argument.
        // This is true for all std::collection elements, that are serialized as lists,
        // so I hope the generic lists from libraries will mostly stick to that convention.
        // As for non-generic lists, there is nothing, we can do to infer the type.
        val possibleInnerTypes = possibleTypes.mapNotNull {
            when (it) {
                is RsTypeAdt -> {
                    val innerType =
                        it.typeArguments.firstOrNull() ?: return@mapNotNull null
                    innerType.substitute(it.typeParameterValues)
                }

                is RsTypeSlice -> {
                    it.elementType.substitute(it.typeParameterValues)
                }

                else -> null
            }
        }.toSet()
        values.forEach {
            infer(it, possibleInnerTypes)
        }
    }

    private fun infer(map: RONMap, possibleTypes: Set<RsType>) {
        val entries = map.mapEntryList

        // We just assume, that the key type is the first type argument and the value type is the second type argument.
        // This is true for all std::collection elements, that are serialized as lists,
        // so I hope the generic maps from libraries will mostly stick to that convention.
        // As for non-generic maps, there is nothing, we can do to infer the type.
        val possibleKeyTypes = possibleTypes.mapNotNull {
            val adt = it as? RsTypeAdt ?: return@mapNotNull null
            val keyType = adt.typeArguments.firstOrNull() ?: return@mapNotNull null
            keyType.substitute(adt.typeParameterValues)
        }.toSet()
        val possibleValueTypes = possibleTypes.mapNotNull {
            val adt = it as? RsTypeAdt ?: return@mapNotNull null
            val valueType =
                adt.typeArguments.drop(1).firstOrNull() ?: return@mapNotNull null
            valueType.substitute(adt.typeParameterValues)
        }.toSet()

        entries.forEach {
            infer(it.mapKey.value, possibleKeyTypes)
            infer(it.value, possibleValueTypes)
        }
    }

    /**
     * Unwraps the extension unwrap_variant_newtypes
     */
    private fun TypeWithFieldOwner.unwrapVariantNewType(): TypeWithFieldOwner? {
        // don't unwrap options, as this is already done by unwrapNewTypes, and we want to avoid getting the same reference twice
        val isOption = type is RsTypeAdt && type.item == fieldOwner.knownItems.Option
        if (fieldOwner !is RsEnumVariant || isOption || fieldOwner.namedFields.isNotEmpty()) return null
        val variantUnwrappedNames = fieldOwner.fields.singleOrNull() ?: return null
        val innerType = variantUnwrappedNames.typeReference?.normType?.substitute(type.typeParameterValues)
        val innerTypeAdt = innerType as? RsTypeAdt ?: return null
        val innerItem = innerTypeAdt.item as? RsStructItem ?: return null
        return TypeWithFieldOwner(innerTypeAdt, innerItem)
    }

    private fun TypeWithFieldOwner.hasFieldNamesByVariantNewType(fieldNames: Collection<String>): Boolean {
        val unwrapped = unwrapVariantNewType() ?: return false
        val allowedInnerFieldNames = unwrapped.fieldOwner.namedFields.mapNotNull { fieldDecl -> fieldDecl.name }
        return allowedInnerFieldNames.containsAll(fieldNames)
    }

    private fun Iterable<TypeWithFieldOwner>.filterByFieldNames(fieldNames: Collection<String>): List<TypeWithFieldOwner> {
        return filter {
            val allowedFieldNames = it.fieldOwner.namedFields.mapNotNull { fieldDecl -> fieldDecl.name }
            allowedFieldNames.containsAll(fieldNames) || it.hasFieldNamesByVariantNewType(fieldNames)
        }
    }

    private fun Iterable<RsType>.filterFieldOwner(): List<TypeWithFieldOwner> {
        return this.filterIsInstance<RsTypeAdt>().flatMap {
            when (val item = it.item) {
                is RsStructItem -> listOf(TypeWithFieldOwner(it, item))
                is RsEnumItem -> item.variants.map { variant -> TypeWithFieldOwner(it, variant) }
                else -> error("${item.javaClass.simpleName} is neither struct nor enum")
            }
        }
    }

    private fun Iterable<RsNamedElement>.filterTypes(): List<RsType> {
        return this.mapNotNull {
            when (it) {
                is RsTypeDeclarationElement -> it.declaredType
                is RsEnumVariant -> it.parentOfType<RsTypeDeclarationElement>(false)?.declaredType
                else -> null
            }
        }
    }

    private fun findNamesInProject(name: String, project: Project): Collection<RsNamedElement> {
        return getElements(
            RsNamedElementIndex.KEY,
            name,
            project,
            GlobalSearchScope.projectScope(project)
        )
    }

    private fun findNamesInGlobalScope(name: String, project: Project): Collection<RsNamedElement> {
        return getElements(
            RsNamedElementIndex.KEY,
            name,
            project,
            GlobalSearchScope.allScope(project)
        ).filter(RsNamedElement::isVisibleToRON)
    }

    private data class RsInferredField(val decl: RsNamedFieldDecl, val rawType: RsType, val normType: RsType) {
        companion object {
            fun fromDecl(decl: RsNamedFieldDecl, ownerSubstitution: Substitution): RsInferredField? {
                val typeReference = decl.typeReference ?: return null
                val rawType = typeReference.rawType.substitute(ownerSubstitution)
                val normType = typeReference.normType.substitute(ownerSubstitution)
                return RsInferredField(decl, rawType, normType)
            }
        }
    }

    /**
     * Unwrap types to handle the extensions implicit_some and unwrap_newtypes
     */
    private fun RsType.unwrapNewTypes(): Set<RsType> {
        if (this is RsTypeAdt) {
            val item = this.item
            if (item is RsStructItem && item.namedFields.isEmpty()) {
                // extension: unwrap_newtypes
                val innerType = item.fields.singleOrNull()?.typeReference
                if (innerType != null) {
                    return setOf(this) + innerType.normType.substitute(this.typeParameterValues).deref().unwrapNewTypes()
                }
            } else if (item == item.knownItems.Option) {
                // extension: implicit_some
                val innerType = this.typeArguments.single().substitute(this.typeParameterValues)
                return setOf(this) + innerType.deref().unwrapNewTypes()
            }
        }
        return setOf(this)
    }

    /**
     * @param fieldOwner May be the type item itself or an enum variant of it
     */
    private data class TypeWithFieldOwner(val type: RsType, val fieldOwner: RsFieldsOwner) {
        operator fun get(index: Int): RsType? {
            val fieldDecl = fieldOwner.fields.getOrNull(index) ?: return null
            return fieldDecl.typeReference?.normType?.substitute(type.typeParameterValues)
        }

        private fun hasAlias(name: String): Boolean {
            val aliases = RsTypeAliasIndex.findPotentialAliases(fieldOwner.project, TyFingerprint.create(type) ?: return false)
            return aliases.any { it.name == name }
        }
        
        fun hasName(name: String): Boolean {
            return fieldOwner.name == name || (fieldOwner !is RsEnumVariant && hasAlias(name))
        }

        fun getAliasOrSelf(name: String): RsNamedElement {
            if (fieldOwner is RsEnumVariant) return fieldOwner
            val aliases = RsTypeAliasIndex.findPotentialAliases(fieldOwner.project, TyFingerprint.create(type) ?: return fieldOwner)
            return aliases.filter { it.alias.isVisibleToRON() }.find { it.name == name }?.alias ?: fieldOwner
        }

        val variants: Set<RsNamedElement> get() {
            if (fieldOwner is RsEnumVariant) return setOf(fieldOwner)
            val tyFingerprint = TyFingerprint.create(type) ?: return setOf(fieldOwner)
            val aliases = RsTypeAliasIndex.findPotentialAliases(fieldOwner.project, tyFingerprint)
            return aliases.map { it.alias }.filter(RsNamedElement::isVisibleToRON).toSet() + fieldOwner
        }
    }

    private fun infer(obj: RONObject, possibleTypes: Set<RsType>) {
        obj.objectBody.valueList.forEach {
            infer(it, emptySet())
        }
        val fieldNameTexts = obj.objectBody.namedFieldList.map { it.fieldName }.map { it.text }
        val name = obj.objectName
        val possibleFieldOwner = possibleTypes.filterFieldOwner()
        val bestMatchingFieldOwners: List<TypeWithFieldOwner> = when (name) {
            null -> {
                possibleFieldOwner.filterByFieldNames(fieldNameTexts).ifEmpty {
                    // If none has matching fields, we use all field owners.
                    // If there are no field owners, we give up. Without a name, there is no efficient way to infer the type.
                    possibleFieldOwner
                }
            }

            else -> {
                val matchByName = possibleFieldOwner.filter { it.hasName(name.text) }
                val matchingByNameAndFields = matchByName.filterByFieldNames(fieldNameTexts)
                matchingByNameAndFields.ifEmpty {
                    matchByName.ifEmpty {
                        val project = obj.project
                        val inProject = findNamesInProject(name.text, project).filterTypes().filterFieldOwner()
                        inProject.filterByFieldNames(fieldNameTexts).ifEmpty {
                            val global = findNamesInGlobalScope(name.text, project).filterTypes().filterFieldOwner()
                            global.filterByFieldNames(fieldNameTexts).ifEmpty {
                                inProject.ifEmpty {
                                    global
                                }
                            }
                        }
                    }
                }
            }
        }
        if (name != null) {
            objects[name] = TypeInferenceResult(
                bestMatchingFieldOwners.map { it.getAliasOrSelf(name.text) },
                possibleFieldOwner.filterByFieldNames(fieldNameTexts).ifEmpty { possibleFieldOwner }.flatMap { it.variants }.toSet().toTypedArray()
            )
        }
        val fieldNameToDecl = bestMatchingFieldOwners.flatMap {
            setOfNotNull(it, it.unwrapVariantNewType())
        }.flatMap {
            it.fieldOwner.namedFields.mapNotNull { decl -> RsInferredField.fromDecl(decl, it.type.typeParameterValues) }
        }.groupBy { it.decl.identifier.text }
        obj.objectBody.namedFieldList.forEach { ronNamedField ->
            val fieldName = ronNamedField.fieldName
            val decls = fieldNameToDecl[fieldName.text].orEmpty()
            fields[fieldName] = FieldInferenceResult(decls.map { it.decl })
            val possibleFieldTypes = decls.map { it.normType }.toSet()
            infer(ronNamedField.value ?: return@forEach, possibleFieldTypes)
        }
    }

    private fun Iterable<RsNamedElement>.filterNamedTuple(): List<TypeWithFieldOwner> {
        return filterTypes().filterFieldOwner().filter { it.fieldOwner.namedFields.isEmpty() }
    }


    private fun RsType.getTupleElement(index: Int): RsType? {
        val rawType = when (this) {
            is RsTypeTuple -> {
                this.types.getOrNull(index)
            }

            is RsTypeArray -> {
                this.base
            }

            is RsTypeAdt -> {
                // this is only called, when the tuple has no name, so we know it cannot be an enum variant
                (this.item as RsStructItem).fields.getOrNull(index)?.typeReference?.normType
            }

            else -> null
        }
        return rawType?.substitute(typeParameterValues)
    }

    private fun infer(tuple: RONTuple, possibleTypes: Set<RsType>) {
        when (val name = tuple.objectName) {
            null -> {
                tuple.tupleBody.valueList.forEachIndexed { index, ronValue ->
                    val possibleElementTypes = possibleTypes.mapNotNull { it.getTupleElement(index) }.toSet()
                    infer(ronValue, possibleElementTypes)
                }
            }

            else -> {
                val nameText = name.text
                val project = name.project
                val possibleFieldOwner = possibleTypes.filterFieldOwner()
                val matchByName = possibleFieldOwner.filter { it.hasName(nameText) }
                val matchByNameAndIsTuple = matchByName.filter { it.fieldOwner.namedFields.isEmpty() }
                val fieldOwner = matchByNameAndIsTuple.ifEmpty {
                    matchByName.ifEmpty {
                        findNamesInProject(nameText, project).filterNamedTuple().ifEmpty {
                            findNamesInGlobalScope(nameText, project).filterNamedTuple()
                        }
                    }
                }
                objects[name] = TypeInferenceResult(
                    fieldOwner.map { it.getAliasOrSelf(name.text) },
                    possibleFieldOwner.flatMap { it.variants }.toSet().toTypedArray(),
                )
                tuple.tupleBody.valueList.forEachIndexed { index, ronValue ->
                    val possibleElementTypes = fieldOwner.mapNotNull { it[index] }.toSet()
                    infer(ronValue, possibleElementTypes)
                }
            }
        }
    }

    private fun infer(objName: RONObjectName, possibleTypes: Set<RsType>) {
        val objNameText = objName.text
        val possibleFieldOwner = possibleTypes.filterFieldOwner()
        val types = possibleFieldOwner.filter {
            it.hasName(objNameText)
        }.mapNotNull {
            when (val fieldOwner = it.fieldOwner) {
                is RsEnumVariant -> fieldOwner
                is RsStructItem -> {
                    if (fieldOwner.blockFields == null && fieldOwner.tupleFields == null) it.getAliasOrSelf(objNameText) else null
                }
                else -> error("${fieldOwner.javaClass.simpleName} is neither struct nor enum variant")
            }
        }.ifEmpty {
            val project = objName.project
            findNamesInProject(objNameText, project).filterIsInstance<RsFieldsOwner>().ifEmpty {
                findNamesInGlobalScope(objNameText, project).filterIsInstance<RsFieldsOwner>()
            }
        }
        objects[objName] = TypeInferenceResult(types, possibleFieldOwner.flatMap { it.variants }.toSet().toTypedArray())
    }
}

fun RsNamedElement.isVisibleToRON(): Boolean {
    val itemElement = parentOfType<RsItemElement>(true)
    if (itemElement != null && itemElement.parent.ancestors.any { it is RsItemElement && it !is RsModItem }) return false
    // We don't check if a public module really is visible here, because we would have to consider re-exports in that case
    if (ancestorOrSelf<RsVisibilityOwner>()?.isPublic != true && containingFile.virtualFile !in GlobalSearchScope.projectScope(project)) return false
    return true
}
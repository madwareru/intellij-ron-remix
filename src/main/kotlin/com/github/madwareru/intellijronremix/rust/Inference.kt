package com.github.madwareru.intellijronremix.rust

import com.github.madwareru.intellijronremix.language.psi.RONFieldName
import com.github.madwareru.intellijronremix.language.psi.RONFile
import com.github.madwareru.intellijronremix.language.psi.RONList
import com.github.madwareru.intellijronremix.language.psi.RONMap
import com.github.madwareru.intellijronremix.language.psi.RONObject
import com.github.madwareru.intellijronremix.language.psi.RONObjectBody
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
import org.rust.lang.core.psi.*
import org.rust.lang.core.psi.ext.*
import org.rust.lang.core.resolve.knownItems
import org.rust.lang.core.stubs.index.RsNamedElementIndex
import org.rust.lang.core.types.Substitution
import org.rust.lang.core.types.infer.substitute
import org.rust.lang.core.types.normType
import org.rust.lang.core.types.rawType
import org.rust.lang.core.types.ty.Ty as RsType
import org.rust.lang.core.types.ty.TyAdt as RsTypeAdt
import org.rust.lang.core.types.ty.TyArray as RsTypeArray
import org.rust.lang.core.types.ty.TyReference as RsTypeRef
import org.rust.lang.core.types.ty.TySlice as RsTypeSlice
import org.rust.lang.core.types.ty.TyTuple as RsTypeTuple

private val RONObjectName.normalizedName: NormalizedName get() = NormalizedName(text.removePrefix("r#"))
private val RONFieldName.normalizedName: NormalizedName get() = NormalizedName(text.removePrefix("r#"))
private val RsNamedFieldDecl.normalizedName: NormalizedName get() = identifier
    .text
    .removePrefix("r#")
    .let { NormalizedName(it) }

private val RsFieldsOwner.normalizedName: NormalizedName? get() = name
    ?.removePrefix("r#")
    ?.let { NormalizedName(it) }

class NormalizedName(private val name: String) {
    /**
     * Name as it would occur in [RsNamedElementIndex]
     */
    val indexedName: String = name

    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        javaClass != other?.javaClass -> false
        else -> {
            other as NormalizedName
            name == other.name
        }
    }

    override fun hashCode(): Int = name.hashCode()
}

private val INFERENCE_KEY: Key<CachedValue<InferenceResult>> = Key.create("RON_TO_RUST_INFERENCE_KEY")

val PsiElement.ronToRustInferenceContext: InferenceResult
    get() = (this.containingFile as RONFile)
            .ronToRustInferenceContext

val RONObjectName.inference: TypeInferenceResult get() = ronToRustInferenceContext
    .objects
    .getValue(this)

val RONFieldName.inference: FieldInferenceResult get() = ronToRustInferenceContext
    .fields
    .getValue(this)

val RONFile.ronToRustInferenceContext: InferenceResult
    get() {
        return CachedValuesManager
            .getManager(project)
            .getCachedValue(
                this,
                INFERENCE_KEY,
                {
                    val result = InferenceBuilder().inferFile(this).finish()

                    when (containingFile.virtualFile) {
                        is VirtualFileWindow -> {
                            CachedValueProvider.Result.create(
                                result,
                                PsiModificationTracker.MODIFICATION_COUNT
                            )
                        }
                        else -> {
                            CachedValueProvider.Result.create(
                                result,
                                listOf(
                                    this.modificationTracker,
                                    project.rustStructureModificationTracker
                                )
                            )
                        }
                    }
                },
                false
            )
    }

data class FieldInferenceResult(
    val possibleFields: List<RsNamedFieldDecl>,
    /**
     * Variants for completion.
     */
    val variants: Array<RsInferredField>,
) {
    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        javaClass != other?.javaClass -> false
        else -> {
            other as FieldInferenceResult
            possibleFields == other.possibleFields && variants.contentEquals(other.variants)
        }
    }

    override fun hashCode(): Int {
        var result = possibleFields.hashCode()
        result = 31 * result + variants.contentHashCode()
        return result
    }
}

data class TypeInferenceResult(
    val possibleDeclarations: List<RsNamedElement>,
    /**
     * Variants for completion. Should only contain completion options with matching type.
     * Other completion options can be calculated elsewhere, because they don't need knowledge from inference.
     */
    val variants: Array<RsNamedElement>,
    val fieldVariants: Set<RsInferredField> = emptySet(),
) {
    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        javaClass != other?.javaClass -> false
        else -> {
            other as TypeInferenceResult
            possibleDeclarations == other.possibleDeclarations && variants.contentEquals(other.variants)
        }
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

data class RsInferredField(val decl: RsNamedFieldDecl, val rawType: RsType, val normType: RsType) {
    val name: NormalizedName = decl.normalizedName

    companion object {
        fun fromDecl(decl: RsNamedFieldDecl, ownerSubstitution: Substitution): RsInferredField? =
            decl.typeReference?.let {
                RsInferredField(
                    decl,
                    it.rawType.substitute(ownerSubstitution),
                    it.normType.substitute(ownerSubstitution)
                )
            }
    }
}

private class InferenceBuilder(
    val fields: MutableMap<RONFieldName, FieldInferenceResult> = mutableMapOf(),
    val objects: MutableMap<RONObjectName, TypeInferenceResult> = mutableMapOf(),
) {
    fun finish(): InferenceResult = InferenceResult(fields, objects)

    fun inferFile(file: RONFile): InferenceBuilder {
        val value = file.childOfType<RONValue>() ?: return this
        inferValue(value, emptySet())
        return this
    }

    /**
     * While reference types are not deserializable, they can be serialized,
     * so we should try to parse them as well.
     * And Box types can be deserialized.
     */
    private fun RsType.deref(): RsType = when {
        this is RsTypeRef -> this.referenced.substitute(this.typeParameterValues).deref()
        this is RsTypeAdt && this.item == this.item.knownItems.Box -> {
            this.typeArguments.first().substitute(this.typeParameterValues).deref()
        }
        else -> this
    }

    private fun Iterable<RsType>.deref(): List<RsType> = map { it.deref() }

    private fun inferValue(
        value: RONValue,
        possibleTypes: Set<RsType>,
        possibleOwnersForFieldVariants: Set<TypeWithFieldOwner> = emptySet()
    ) = possibleTypes
            .deref()
            .flatMap { it.unwrapNewTypes() }
            .toSet()
            .let { adaptedTypes ->
                when (val child = value.children.singleOrNull()) {
                    is RONOption -> inferOption(child, adaptedTypes)
                    is RONList -> inferList(child, adaptedTypes)
                    is RONMap -> inferMap(child, adaptedTypes)
                    is RONObject -> inferObject(child, adaptedTypes)
                    is RONTuple -> inferTuple(child, adaptedTypes)
                    is RONObjectName -> inferObjectName(child, adaptedTypes, possibleOwnersForFieldVariants)
                }
            }

    private fun inferOption(option: RONOption, possibleTypes: Set<RsType>) {
        option.value?.let { someBody ->
            possibleTypes
                .mapNotNull {
                    when {
                        it is RsTypeAdt && it.item == it.item.knownItems.Option -> {
                            val innerType = it.typeArguments.single()
                            innerType.substitute(it.typeParameterValues)
                        }
                        else -> null
                    }
                }
                .toSet()
                .let { possibleInnerTypes -> inferValue(someBody, possibleInnerTypes) }
        }
    }

    private fun inferList(list: RONList, possibleTypes: Set<RsType>) {
        // We just assume, that the inner type is the first type argument.
        // This is true for all std::collection elements, that are serialized as lists,
        // so we hope the generic lists from libraries will mostly stick to that convention.
        // As for non-generic lists, there is nothing, we can do to infer the type.
        val possibleInnerTypes = possibleTypes
            .mapNotNull {
                when (it) {
                    is RsTypeAdt -> it.typeArguments.firstOrNull()?.substitute(it.typeParameterValues)
                    is RsTypeSlice -> it.elementType.substitute(it.typeParameterValues)
                    else -> null
                }
            }
            .toSet()

        list.valueList.forEach { inferValue(it, possibleInnerTypes) }
    }

    private fun inferMap(map: RONMap, possibleTypes: Set<RsType>) {
        // We just assume, that the key type is the first type argument and the value type is the second type argument.
        // This is true for all std::collection elements, that are serialized as lists,
        // so I hope the generic maps from libraries will mostly stick to that convention.
        // As for non-generic maps, there is nothing, we can do to infer the type.
        val possibleKeyTypes = possibleTypes
            .mapNotNull {
                when {
                    it is RsTypeAdt -> it.typeArguments.firstOrNull()?.substitute(it.typeParameterValues)
                    else -> null
                }
            }.toSet()

        val possibleValueTypes = possibleTypes.mapNotNull {
            when {
                it is RsTypeAdt -> it.typeArguments.drop(1).firstOrNull()?.substitute(it.typeParameterValues)
                else -> null
            }
        }.toSet()

        map.mapEntryList.forEach {
            inferValue(it.mapKey.value, possibleKeyTypes)
            inferValue(it.value, possibleValueTypes)
        }
    }

    /**
     * Unwraps the extension unwrap_variant_newtypes
     */
    private fun TypeWithFieldOwner.unwrapVariantNewType() =
        // don't unwrap options, as this is already done by unwrapNewTypes,
        // and we want to avoid getting the same reference twice
        when {
            type is RsTypeAdt && type.item == fieldOwner.knownItems.Option -> null
            fieldOwner !is RsEnumVariant -> null
            fieldOwner.namedFields.isNotEmpty() -> null
            else -> { fieldOwner
                .fields
                .singleOrNull()
                ?.typeReference
                ?.normType
                ?.substitute(type.typeParameterValues)
                ?.let { innerType -> (innerType as? RsTypeAdt)
                ?.let { innerTypeAdt -> (innerTypeAdt.item as? RsStructItem)
                ?.let { innerItem -> TypeWithFieldOwner(innerTypeAdt, innerItem) } } }
            }
        }

    private fun TypeWithFieldOwner.hasFieldNamesByVariantNewType(fieldNames: Collection<NormalizedName>) =
        unwrapVariantNewType()
            ?.fieldOwner
            ?.namedFields
            ?.map { fieldDeclaration -> fieldDeclaration.normalizedName }
            ?.containsAll(fieldNames)
            ?: false

    private fun Iterable<TypeWithFieldOwner>.filterByFieldNames(fieldNames: Collection<NormalizedName>) =
        filter { typeWithFieldOwner ->
            typeWithFieldOwner.hasFieldNamesByVariantNewType(fieldNames) ||
            typeWithFieldOwner
                .fieldOwner
                .namedFields
                .map { fieldDeclaration -> fieldDeclaration.normalizedName }
                .containsAll(fieldNames)
        }

    private fun Iterable<RsType>.filterFieldOwner() =
        this.filterIsInstance<RsTypeAdt>()
            .flatMap {
                when (val item = it.item) {
                    is RsStructItem -> listOf(TypeWithFieldOwner(it, item))
                    is RsEnumItem -> item.variants.map { variant -> TypeWithFieldOwner(it, variant) }
                    else -> error("${item.javaClass.simpleName} is neither struct nor enum")
                }
            }

    private fun Iterable<RsNamedElement>.filterTypes() =
        this.mapNotNull {
            when (it) {
                is RsStructItem -> it.declaredType
                is RsEnumVariant -> it.parentOfType<RsTypeDeclarationElement>(false)?.declaredType
                else -> null
            }
        }

    private fun findNamesInProject(name: NormalizedName, project: Project) =
        RsNamedElementIndex
            .findElementsByName(project, name.indexedName, GlobalSearchScope.projectScope(project))

    private fun findNamesInGlobalScope(name: NormalizedName, project: Project) =
        RsNamedElementIndex.findElementsByName(project, name.indexedName, GlobalSearchScope.allScope(project))
            .filter(RsNamedElement::isVisibleToRON)

    /**
     * Unwrap types to handle the extensions implicit_some and unwrap_newtypes
     */
    private fun RsType.unwrapNewTypes(): Set<RsType> = setOf(this) +
        if (this is RsTypeAdt) {
            val item = this.item
            when {
                item is RsStructItem && item.namedFields.isEmpty() -> {
                    // extension: unwrap_newtypes
                    item.fields
                        .singleOrNull()
                        ?.typeReference
                        ?.normType
                        ?.substitute(this.typeParameterValues)
                        ?.deref()
                        ?.unwrapNewTypes()
                        ?: emptySet()
                }
                item == item.knownItems.Option -> {
                    this.typeArguments
                        .single()
                        .substitute(this.typeParameterValues)
                        .deref()
                        .unwrapNewTypes()
                }
                else -> emptySet()
            }
        } else {
            emptySet()
        }

    /**
     * @param fieldOwner Can be the type item itself or an enum variant of it
     */
    private data class TypeWithFieldOwner(val type: RsType, val fieldOwner: RsFieldsOwner) {
        operator fun get(index: Int): RsType? = fieldOwner
            .fields
            .getOrNull(index)
            ?.typeReference
            ?.normType
            ?.substitute(type.typeParameterValues)

        fun hasName(name: NormalizedName): Boolean = fieldOwner.normalizedName == name
    }

    private fun inferObject(obj: RONObject, possibleTypes: Set<RsType>) {
        val fieldNameTexts = obj.objectBody
            .namedFieldList
            .map { it.fieldName.normalizedName }

        val objName = obj.objectName

        val possibleFieldOwner = possibleTypes.filterFieldOwner()

        val bestMatchingFieldOwners: List<TypeWithFieldOwner> =
           objName?.let { name ->
               val matchByName = possibleFieldOwner.filter { it.hasName(name.normalizedName) }
               val matchingByNameAndFields = matchByName.filterByFieldNames(fieldNameTexts)
               matchingByNameAndFields.ifEmpty {
                   matchByName.ifEmpty {
                       val project = obj.project
                       val inProject = findNamesInProject(name.normalizedName, project).filterTypes().filterFieldOwner()
                       inProject.filterByFieldNames(fieldNameTexts).ifEmpty {
                           val global = findNamesInGlobalScope(name.normalizedName, project).filterTypes().filterFieldOwner()
                           global.filterByFieldNames(fieldNameTexts).ifEmpty {
                               inProject.ifEmpty {
                                   global
                               }
                           }
                       }
                   }
               }
           }
           ?: possibleFieldOwner
               .filterByFieldNames(fieldNameTexts)
               .ifEmpty {
                   // If none has matching fields, we use all field owners.
                   // If there are no field owners, we give up.
                   // Without a name, there is no efficient way to infer the type.
                   possibleFieldOwner
               }

        val used = obj.objectBody
            .namedFieldList
            .map { it.fieldName.normalizedName }

        if (objName != null) {
            val possibleDeclarations = bestMatchingFieldOwners.map { it.fieldOwner }
            val variants : Array<RsNamedElement> = possibleFieldOwner
                .filterByFieldNames(fieldNameTexts)
                .ifEmpty { possibleFieldOwner }
                .map { it.fieldOwner }
                .filter { it.normalizedName !in used }
                .toSet().toTypedArray()

            objects[objName] = TypeInferenceResult(possibleDeclarations, variants)
        }

        val inferredFields = bestMatchingFieldOwners
            .flatMap { setOfNotNull(it, it.unwrapVariantNewType()) }
            .flatMap { it.fieldOwner
                .namedFields
                .mapNotNull { declaration -> RsInferredField.fromDecl(declaration, it.type.typeParameterValues) }
            }

        val fieldNameToDeclaration = inferredFields.groupBy { it.name }

        obj.objectBody.namedFieldList.forEach { ronNamedField ->
            val fieldName = ronNamedField.fieldName
            val declarations = fieldNameToDeclaration[fieldName.normalizedName].orEmpty()
            // Propose unused fields or the currently matching fields
            val possibleFields = declarations.map { it.decl }

            val variants = inferredFields
                .filter { it.name !in used || it.name in declarations.map { it.name } }
                .toTypedArray()

            fields[fieldName] = FieldInferenceResult(possibleFields, variants)

            val possibleFieldTypes = declarations
                .map { it.normType }
                .toSet()

            inferValue(ronNamedField.value ?: return@forEach, possibleFieldTypes)
        }
    }


    private fun RsType.getTupleElement(index: Int) = when (this) {
        is RsTypeTuple -> listOfNotNull(this.types.getOrNull(index))
        is RsTypeArray -> listOf(this.base)
        is RsTypeAdt -> {
            when (val item = this.item) {
                is RsStructItem -> listOfNotNull(item.fields.getOrNull(index)?.typeReference?.normType)
                is RsEnumItem -> item.variants.mapNotNull { it.fields.getOrNull(index)?.typeReference?.normType }
                else -> error("Rust ADT item was neither struct nor enum")
            }
        }
        else -> emptyList()
    }.map { it.substitute(typeParameterValues) }

    private fun inferTuple(tuple: RONTuple, possibleTypes: Set<RsType>) {
        when (val name = tuple.objectName) {
            null -> {
                val possibleOwnersForFieldVariants = possibleTypes
                    .filterFieldOwner()
                    .toSet()

                tuple.tupleBody.valueList.forEachIndexed { index, ronValue ->
                    val possibleElementTypes = possibleTypes
                        .flatMap { it.getTupleElement(index) }
                        .toSet()

                    inferValue(ronValue, possibleElementTypes, possibleOwnersForFieldVariants)
                }
            }
            else -> {
                val nameText = name.normalizedName
                val project = name.project
                val possibleFieldOwner = possibleTypes.filterFieldOwner()
                val matchByName = possibleFieldOwner.filter { it.hasName(nameText) }
                val matchByNameAndIsTuple = matchByName.filter { it.fieldOwner.namedFields.isEmpty() }
                val fieldOwner = matchByNameAndIsTuple.ifEmpty {
                    matchByName.ifEmpty {
                        // We don't test, if these are tuples, because it might be an object,
                        // and the user, just didn't type the fields yet.
                        findNamesInProject(nameText, project)
                            .filterTypes()
                            .filterFieldOwner()
                            .ifEmpty {
                                findNamesInGlobalScope(nameText, project)
                                    .filterTypes()
                                    .filterFieldOwner()
                            }
                    }
                }

                val possibleDeclarations = fieldOwner.map { it.fieldOwner }
                val variants: Array<RsNamedElement> = possibleFieldOwner
                    .map { it.fieldOwner }
                    .toSet()
                    .toTypedArray()

                objects[name] = TypeInferenceResult(possibleDeclarations, variants)

                val possibleOwnersForFieldVariants = fieldOwner.toSet()
                tuple.tupleBody
                    .valueList
                    .forEachIndexed { index, ronValue ->
                        val possibleElementTypes = fieldOwner.mapNotNull { it[index] }.toSet()
                        inferValue(ronValue, possibleElementTypes, possibleOwnersForFieldVariants)
                    }
            }
        }
    }

    private fun inferObjectName(
        objName: RONObjectName,
        possibleTypes: Set<RsType>,
        possibleOwnersForFieldVariants: Set<TypeWithFieldOwner>
    ) {
        val objNameText = objName.normalizedName
        val possibleFieldOwner = possibleTypes.filterFieldOwner()

        val types = possibleFieldOwner
            .filter { it.hasName(objNameText) }
            .mapNotNull {
                when (val fieldOwner = it.fieldOwner) {
                    is RsEnumVariant -> fieldOwner
                    is RsStructItem -> {
                        if (fieldOwner.blockFields == null && fieldOwner.tupleFields == null) {
                            it.fieldOwner
                        } else {
                            null
                        }
                    }
                    else -> error("${fieldOwner.javaClass.simpleName} is neither struct nor enum variant")
                }
            }.ifEmpty {
                val project = objName.project
                findNamesInProject(objNameText, project)
                    .filterIsInstance<RsFieldsOwner>()
                    .ifEmpty {
                        findNamesInGlobalScope(objNameText, project)
                            .filterIsInstance<RsFieldsOwner>()
                    }
            }

        val usedFieldNames = objName
            .parentOfType<RONObjectBody>(false)
            ?.namedFieldList.orEmpty()
            .map { it.fieldName.normalizedName }

        val variants : Array<RsNamedElement> = possibleFieldOwner
            .map { it.fieldOwner }
            .toSet()
            .toTypedArray()

        val fieldVariants = possibleOwnersForFieldVariants
            .flatMap { setOfNotNull(it, it.unwrapVariantNewType()) }
            .flatMap { typeWithFieldOwner ->
                val typeParamValues = typeWithFieldOwner.type.typeParameterValues
                typeWithFieldOwner.fieldOwner
                        .namedFields
                        .mapNotNull { RsInferredField.fromDecl(it, typeParamValues) }
                        .filter { namedField -> namedField.name !in usedFieldNames }
            }.toSet()

        objects[objName] = TypeInferenceResult(types, variants, fieldVariants)
    }
}

fun RsNamedElement.isVisibleToRON(): Boolean =
    // We don't check if a public module really is visible here,
    // because we would have to consider re-exports in that case
    containingFile.virtualFile in GlobalSearchScope.projectScope(project) ||
        (ancestorOrSelf<RsVisibilityOwner>()?.isPublic ?: false) ||
        (parentOfType<RsItemElement>(true)
            ?.parent
            ?.ancestors
            ?.all { it is RsItemElement && it is RsModItem }
            ?: false
        )
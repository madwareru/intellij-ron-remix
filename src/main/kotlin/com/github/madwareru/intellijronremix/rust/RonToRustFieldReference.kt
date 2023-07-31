package com.github.madwareru.intellijronremix.rust

import com.github.madwareru.intellijronremix.language.psi.RONFieldName
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.parentOfType
import org.rust.lang.core.psi.ext.RsMod
import org.rust.lang.core.psi.ext.RsNamedElement

class RonToRustFieldReference(ronFieldName: RONFieldName) : RonToRustReferenceCached<RONFieldName>(ronFieldName) {
    companion object {
        fun createLookupItem(element: RsInferredField) =
            element.decl.parentOfType<RsMod>()?.let { nameElement ->
                val crate = nameElement.containingCrate.normName
                val path = nameElement.crateRelativePath
                val fullPath = " in $crate$path"
                LookupElementBuilder.create(element.decl)
                    .withTypeText(element.normType.toString(), true)
                    .withTailText(fullPath, true)
            }
            ?: LookupElementBuilder
                .create(element.decl)
                .withTypeText(element.normType.toString(), true)
    }

    override fun getVariants(): Array<LookupElement> = when {
        DumbService.isDumb(element.project) -> emptyArray()
        else -> element.inference.variants.map(::createLookupItem).toTypedArray()
    }

    override fun calculateDefaultRangeInElement() = TextRange(0, element.textLength)

    // if existing, we assume a type from your own project is meant, even if a type of the same name exists in other projects
    override fun resolveInner() = element.inference.possibleFields
}

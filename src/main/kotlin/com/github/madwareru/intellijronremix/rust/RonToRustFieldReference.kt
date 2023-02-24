package com.github.madwareru.intellijronremix.rust

import com.github.madwareru.intellijronremix.language.psi.RONFieldName
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.parentOfType
import org.rust.lang.core.psi.ext.RsMod
import org.rust.lang.core.psi.ext.RsNamedElement

class RonToRustFieldReference(ronFieldName: RONFieldName) : RonToRustReferenceCached<RONFieldName>(ronFieldName) {
    companion object {
        fun createLookupItem(element: RsInferredField): LookupElement {
            val nameElement = element.decl.parentOfType<RsMod>()
                ?: return LookupElementBuilder.create(element.decl).withTypeText(element.normType.toString(), true)
            val crate = nameElement.containingCrate.normName
            val path = nameElement.crateRelativePath
            val fullPath = " in $crate$path"
            return LookupElementBuilder.create(element.decl)
                .withTypeText(element.normType.toString(), true)
                .withTailText(fullPath, true)
        }
    }

    override fun getVariants(): Array<LookupElement> {
        return element.inference.variants.map(::createLookupItem).toTypedArray()
    }

    override fun calculateDefaultRangeInElement(): TextRange {
        return TextRange(0, element.textLength)
    }

    // if existing, we assume a type from your own project is meant, even if a type of the same name exists in other projects
    override fun resolveInner(): Collection<RsNamedElement> {
        return element.inference.possibleFields
    }
}

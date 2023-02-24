package com.github.madwareru.intellijronremix.rust

import com.github.madwareru.intellijronremix.language.psi.RONObjectName
import com.intellij.codeInsight.completion.CompletionUtilCore
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.util.TextRange
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.util.parentOfType
import org.rust.lang.core.psi.ext.*
import org.rust.lang.core.stubs.index.RsNamedElementIndex

class RonToRustTypeReference(ronObjectName: RONObjectName) : RonToRustReferenceCached<RONObjectName>(ronObjectName) {
    private fun createLookupItem(element: RsNamedElement): LookupElement {
        val nameElement = element.parentOfType<RsMod>() ?: return LookupElementBuilder.create(element)
        val crate = nameElement.containingCrate.normName
        val path = nameElement.crateRelativePath
        val fullPath = "$crate$path"
        return LookupElementBuilder.create(element)
            .withTypeText(fullPath, true)
    }

    override fun getVariants(): Array<LookupElement> {
        if (DumbService.isDumb(element.project)) return emptyArray()
        val inference = element.inference
        val typeVariants = inference.variants.map(::createLookupItem).toTypedArray()
        val fieldVariants = inference.fieldVariants.map(RonToRustFieldReference::createLookupItem).toTypedArray()
        return (typeVariants + fieldVariants).ifEmpty {
            val rawText = element.text.removeSuffix(CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED)
            val project = element.project
            val seq: Sequence<RsNamedElement> = if (rawText.isEmpty()) {
                // only use project scope, for performance
                val scope = GlobalSearchScope.projectScope(project)
                StubIndex.getInstance().getAllKeys(RsNamedElementIndex.KEY, project)
                    .asSequence()
                    // avoid most generics early
                    .filter { it.length >= 2 }
                    .flatMap {
                        RsNamedElementIndex.findElementsByName(project, it, scope)
                            .filterIsInstance<RsFieldsOwner>()
                            .filter(RsFieldsOwner::isVisibleToRON)
                    }
            } else {
                val scope = GlobalSearchScope.allScope(project)
                val start = element.text[0]
                StubIndex.getInstance().getAllKeys(RsNamedElementIndex.KEY, project)
                    .asSequence()
                    // avoid most generics early
                    .filter { it.length >= 2 }
                    // generally intellij should decide if the names match,
                    // but we have to require at least the first letter, for performance reasons
                    .filter { it.startsWith(start) }
                    .flatMap {
                        val elementsByName = RsNamedElementIndex.findElementsByName(project, it, scope)
                        elementsByName
                            .filterIsInstance<RsFieldsOwner>()
                            .filter(RsFieldsOwner::isVisibleToRON)
                    }
            }
            seq
                .map(::createLookupItem)
                .toList()
                .toTypedArray()
        }
    }

    override fun calculateDefaultRangeInElement(): TextRange {
        return TextRange(0, element.textLength)
    }

    // if existing, we assume a type from your own project is meant, even if a type of the same name exists in other projects
    override fun resolveInner(): Collection<RsNamedElement> {
        return element.inference.possibleDeclarations
    }
}

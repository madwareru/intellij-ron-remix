package com.github.madwareru.intellijronremix.ide.annotator.checkers

import com.github.madwareru.intellijronremix.language.psi.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.psi.PsiElement

class RONObjectCheckerAnnotator : CheckerAnnotator()  {
    override fun check(element: PsiElement, holder: AnnotationHolder): CheckerAnnotatorResult =
        if (holder.isBatchMode) {
            CheckerAnnotatorResult.Ok
        } else {
            when (element) {
                is RONObjectEntry -> checkObjectEntry(element)
                else -> CheckerAnnotatorResult.Ok
            }
        }

    private fun checkObjectEntry(objectEntry: RONObjectEntry): CheckerAnnotatorResult {
        val filteredEntries = (objectEntry.parent as RONObjectBody)
            .objectEntryList
            .asSequence()
            .filterNot { it == objectEntry }

        val duplicatesFound = filteredEntries.any { it.keyTextMatches(objectEntry.keyText) }

        return if (duplicatesFound) {
            CheckerAnnotatorResult.Error(
                "Duplicate keys found in an object",
                objectEntry.namedField!!.ident.textRange
            )
        } else {
            CheckerAnnotatorResult.Ok
        }
    }
}


package com.github.madwareru.intellijronremix.ide.annotator.checkers

import com.github.madwareru.intellijronremix.language.psi.RONObjectBody
import com.github.madwareru.intellijronremix.language.psi.RONObjectEntry
import com.github.madwareru.intellijronremix.language.psi.isTuple
import com.github.madwareru.intellijronremix.language.psi.keyText
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.psi.PsiElement

class RONObjectCheckerAnnotator : CheckerAnnotator()  {
    override fun check(element: PsiElement, holder: AnnotationHolder): CheckerAnnotatorResult =
        if (holder.isBatchMode) {
            CheckerAnnotatorResult.Ok
        } else {
            when (element) {
                is RONObjectBody -> checkObjectBody(element)
                is RONObjectEntry -> checkObjectEntry(element)
                else -> CheckerAnnotatorResult.Ok
            }
        }

    private fun checkObjectEntry(objectEntry: RONObjectEntry): CheckerAnnotatorResult {
        val filteredEntries = (objectEntry.parent as RONObjectBody)
            .objectEntryList
            .asSequence()
            .filterNot { it == objectEntry }

        val duplicatesFound = filteredEntries.any { it.keyText != null && it.keyText == objectEntry.keyText }

        return if (duplicatesFound) {
            CheckerAnnotatorResult.Error(
                "Duplicate keys found in an object",
                objectEntry.namedField!!.ident.textRange
            )
        } else {
            CheckerAnnotatorResult.Ok
        }
    }

    private fun checkObjectBody(objectBody: RONObjectBody): CheckerAnnotatorResult {
        val head = objectBody.objectEntryList.firstOrNull()
        val tail = objectBody.objectEntryList.asSequence().drop(1)
        return when {
            head == null || tail.count() == 0 -> CheckerAnnotatorResult.Ok
            else -> checkObjectBodyImpl(head, tail)
        }
    }

    private fun checkObjectBodyImpl(head: RONObjectEntry, tail: Sequence<RONObjectEntry>) =
        if (head.isTuple) {
            when(val found = tail.find { !it.isTuple }) {
                null -> CheckerAnnotatorResult.Ok
                else -> CheckerAnnotatorResult.Error("Tuple object has non-tuple elements", found.textRange)
            }
        }
        else {
            when(val found = tail.find { it.isTuple }) {
                null -> CheckerAnnotatorResult.Ok
                else -> CheckerAnnotatorResult.Error("Non-tuple object has tuple elements", found.textRange)
            }
        }
}


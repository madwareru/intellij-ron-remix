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
                is RONNamedField -> checkObjectEntry(element)
                is RONValue -> checkValue(element)
                else -> CheckerAnnotatorResult.Ok
            }
        }

    private fun checkValue(value: RONValue): CheckerAnnotatorResult {
        if (value.parent is RONObjectBody) {
            return CheckerAnnotatorResult.Error("Object entry must have a field name", value.textRange)
        }
        return CheckerAnnotatorResult.Ok
    }

    private fun checkObjectEntry(objectEntry: RONNamedField): CheckerAnnotatorResult {
        val filteredEntries = (objectEntry.parent as RONObjectBody)
            .namedFieldList
            .asSequence()
            .filterNot { it == objectEntry }

        val namedField = objectEntry.fieldName
        val keyText = namedField.text
        val duplicatesFound = filteredEntries.any { it.keyTextMatches(keyText) }

        return if (duplicatesFound) {
            CheckerAnnotatorResult.Error(
                "Duplicate keys found in an object",
                namedField.textRange
            )
        } else {
            CheckerAnnotatorResult.Ok
        }
    }
}


package com.github.madwareru.intellijronremix.ide.annotator.checkers

import com.github.madwareru.intellijronremix.language.psi.RONMap
import com.github.madwareru.intellijronremix.language.psi.RONMapEntry
import com.github.madwareru.intellijronremix.language.psi.keyAsText
import com.github.madwareru.intellijronremix.language.psi.keyAsTextMatches
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.psi.PsiElement

class RONMapCheckerAnnotator : CheckerAnnotator()  {
    override fun check(element: PsiElement, holder: AnnotationHolder): CheckerAnnotatorResult =
        if (holder.isBatchMode) {
            CheckerAnnotatorResult.Ok
        } else {
            when (element) {
                is RONMapEntry -> checkMapEntry(element)
                else -> CheckerAnnotatorResult.Ok
            }
        }

    private fun checkMapEntry(mapEntry: RONMapEntry): CheckerAnnotatorResult {
        val filteredEntries = (mapEntry.parent as RONMap)
            .mapEntryList
            .asSequence()
            .filterNot { it == mapEntry }

        val duplicatesFound = filteredEntries.any { mapEntry.keyAsTextMatches(it.keyAsText) }

        return if (duplicatesFound) {
            CheckerAnnotatorResult.Error(
                "Duplicate keys found in a dictionary",
                mapEntry.mapKey.textRange
            )
        } else {
            CheckerAnnotatorResult.Ok
        }
    }
}
package com.github.madwareru.intellijronremix.ide.braces

import com.github.madwareru.intellijronremix.language.RONFileType
import com.github.madwareru.intellijronremix.language.RONLanguage
import com.intellij.codeInsight.highlighting.PairedBraceMatcherAdapter
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.openapi.fileTypes.FileType

class RONBraceMatcher : PairedBraceMatcherAdapter(RONBaseBraceMatcher(), RONLanguage.INSTANCE) {
    override fun isLBraceToken(iterator: HighlighterIterator, fileText: CharSequence, fileType: FileType): Boolean
        = isBrace(iterator, fileText, fileType, true)

    override fun isRBraceToken(iterator: HighlighterIterator, fileText: CharSequence, fileType: FileType): Boolean
            = isBrace(iterator, fileText, fileType, false)

    private fun isBrace(
        iterator: HighlighterIterator,
        fileText: CharSequence,
        fileType: FileType,
        left: Boolean
    ) : Boolean {
        if (fileType != RONFileType.INSTANCE) return false
        val pair = findPair(left, iterator, fileText, fileType)
        return pair != null
    }
}
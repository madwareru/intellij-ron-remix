package com.github.madwareru.intellijronremix.language.todo

import com.github.madwareru.intellijronremix.language.RONLexerAdapter
import com.github.madwareru.intellijronremix.language.psi.RONFile
import com.github.madwareru.intellijronremix.language.psi.RON_COMMENTS
import com.intellij.lexer.Lexer
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.search.IndexPatternBuilder
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

class RONTodoIndexPatternBuilder : IndexPatternBuilder {
    override fun getIndexingLexer(file: PsiFile): Lexer? =
        if (file is RONFile) RONLexerAdapter() else null

    override fun getCommentTokenSet(file: PsiFile): TokenSet? =
        if (file is RONFile) RON_COMMENTS else null

    override fun getCommentStartDelta(tokenType: IElementType?): Int =
        if (tokenType in RON_COMMENTS) 2 else 0

    override fun getCommentEndDelta(tokenType: IElementType?): Int = 0
}
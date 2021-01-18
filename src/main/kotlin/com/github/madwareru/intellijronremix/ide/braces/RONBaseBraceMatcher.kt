package com.github.madwareru.intellijronremix.ide.braces

import com.github.madwareru.intellijronremix.language.psi.RONTypes.*
import com.github.madwareru.intellijronremix.language.psi.RON_COMMENTS
import com.github.madwareru.intellijronremix.language.psi.tokenSetOf
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

class RONBaseBraceMatcher : PairedBraceMatcher {
    override fun getPairs(): Array<BracePair> = PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, next: IElementType?): Boolean =
        next in InsertPairBraceBefore

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset

    companion object {
        private val PAIRS = arrayOf(
            BracePair(BRACEL, BRACER, true),
            BracePair(BRACKETL, BRACKETR, true),
            BracePair(PARENTHESISL, PARENTHESISR, true)
        )

        private val InsertPairBraceBefore = TokenSet.orSet(
            RON_COMMENTS,
            tokenSetOf(
                TokenType.WHITE_SPACE,
                COMMA,
                PARENTHESISR,
                BRACKETR,
                BRACER,
                BRACEL
            )
        )
    }
}
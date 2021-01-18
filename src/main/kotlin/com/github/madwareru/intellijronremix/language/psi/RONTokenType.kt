package com.github.madwareru.intellijronremix.language.psi

import com.github.madwareru.intellijronremix.language.RONLanguage
import com.github.madwareru.intellijronremix.language.psi.RONTypes.BLOCK_COMMENT
import com.github.madwareru.intellijronremix.language.psi.RONTypes.COMMENT
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

class RONTokenType(debugName: String) : IElementType(debugName, RONLanguage.INSTANCE) {
    override fun toString(): String = "RONTokenType.${super.toString()}"
}

fun tokenSetOf(vararg tokens: IElementType) = TokenSet.create(*tokens)

val RON_COMMENTS = tokenSetOf(BLOCK_COMMENT, COMMENT)
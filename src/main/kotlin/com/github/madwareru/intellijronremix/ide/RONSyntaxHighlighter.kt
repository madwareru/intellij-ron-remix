package com.github.madwareru.intellijronremix.ide

import com.github.madwareru.intellijronremix.ide.colors.RONColor
import com.github.madwareru.intellijronremix.language.RONLexerAdapter
import com.github.madwareru.intellijronremix.language.psi.RONTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class RONSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        return RONLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return pack(getTokenColor(tokenType)?.textAttributesKey)
    }

    private fun getTokenColor(tokenType: IElementType): RONColor? {
        return when (tokenType) {
            RONTypes.COLON -> RONColor.COLON
            RONTypes.COMMA -> RONColor.COMMA
            RONTypes.BOOLEAN -> RONColor.BOOLEAN
            RONTypes.IDENT -> RONColor.IDENTIFIER
            TokenType.BAD_CHARACTER -> RONColor.BAD_CHAR
            else -> getTokenColorSpecial(tokenType)
        }
    }

    private fun getTokenColorSpecial(tokenType: IElementType): RONColor? {
        return when (tokenType) {
            RONTypes.PARENTHESISL, RONTypes.PARENTHESISR -> RONColor.PARENTHESES
            RONTypes.BRACKETL, RONTypes.BRACKETR -> RONColor.BRACKETS
            RONTypes.BRACEL, RONTypes.BRACER -> RONColor.BRACES
            RONTypes.INTEGER, RONTypes.FLOAT -> RONColor.NUMBER
            RONTypes.STRING, RONTypes.RAW_STRING -> RONColor.STRING
            RONTypes.SOME, RONTypes.NONE -> RONColor.OPTION
            RONTypes.COMMENT, RONTypes.BLOCK_COMMENT -> RONColor.COMMENT
            else -> null
        }
    }
}

package com.github.madwareru.intellijronremix.ide

import com.github.madwareru.intellijronremix.language.RONLexerAdapter
import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts.BAD_CHAR_KEYS
import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts.BOOLEAN_KEYS
import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts.BRACES_KEYS
import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts.BRACKETS_KEYS
import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts.COLON_KEYS
import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts.COMMA_KEYS
import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts.COMMENT_KEYS
import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts.EMPTY_KEYS
import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts.EXTENSION_KEYS
import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts.IDENT_KEYS
import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts.NUMBER_KEYS
import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts.OPTION_KEYS
import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts.PARENTHESES_KEYS
import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts.STRING_KEYS
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

    override fun getTokenHighlights(tokenType: IElementType): Array<out TextAttributesKey?> {
        return when (tokenType) {
            RONTypes.COLON -> {
                COLON_KEYS
            }
            RONTypes.COMMA -> {
                COMMA_KEYS
            }
            RONTypes.PARENTHESISL, RONTypes.PARENTHESISR -> {
                PARENTHESES_KEYS
            }
            RONTypes.BRACKETL, RONTypes.BRACKETR -> {
                BRACKETS_KEYS
            }
            RONTypes.BRACEL, RONTypes.BRACER -> {
                BRACES_KEYS
            }
            RONTypes.BOOLEAN -> {
                BOOLEAN_KEYS
            }
            RONTypes.INTEGER, RONTypes.FLOAT -> {
                NUMBER_KEYS
            }
            RONTypes.STRING, RONTypes.RAW_STRING -> {
                STRING_KEYS
            }
            RONTypes.SOME, RONTypes.NONE -> {
                OPTION_KEYS
            }
            RONTypes.EXTENSION -> {
                EXTENSION_KEYS
            }
            RONTypes.IDENT -> {
                IDENT_KEYS
            }
            RONTypes.COMMENT, RONTypes.BLOCK_COMMENT -> {
                COMMENT_KEYS
            }
            TokenType.BAD_CHARACTER -> {
                BAD_CHAR_KEYS
            }
            else -> {
                EMPTY_KEYS
            }
        }
    }
}
package com.github.madwareru.intellijronremix.ide

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

object RONSyntaxHighlighterConsts {
    val COLON = TextAttributesKey.createTextAttributesKey(
        "RON_COLON",
        DefaultLanguageHighlighterColors.OPERATION_SIGN
    )
    val COMMA = TextAttributesKey.createTextAttributesKey(
        "RON_COMMA",
        DefaultLanguageHighlighterColors.COMMA
    )
    val PARENTHESES = TextAttributesKey.createTextAttributesKey(
        "RON_PARENTHESES",
        DefaultLanguageHighlighterColors.PARENTHESES
    )
    val BRACKETS = TextAttributesKey.createTextAttributesKey(
        "RON_BRACKETS",
        DefaultLanguageHighlighterColors.BRACKETS
    )
    val BRACES = TextAttributesKey.createTextAttributesKey(
        "RON_BRACES",
        DefaultLanguageHighlighterColors.BRACES
    )
    val BOOLEAN = TextAttributesKey.createTextAttributesKey(
        "RON_BOOLEAN",
        DefaultLanguageHighlighterColors.KEYWORD
    )
    val NUMBER = TextAttributesKey.createTextAttributesKey(
        "RON_NUMBER",
        DefaultLanguageHighlighterColors.NUMBER
    )
    val STRING = TextAttributesKey.createTextAttributesKey(
        "RON_STRING",
        DefaultLanguageHighlighterColors.STRING
    )
    val OPTION = TextAttributesKey.createTextAttributesKey(
        "RON_OPTION",
        DefaultLanguageHighlighterColors.KEYWORD
    )
    val EXTENSION = TextAttributesKey.createTextAttributesKey(
        "RON_EXTENSION",
        DefaultLanguageHighlighterColors.METADATA
    )
    val IDENT = TextAttributesKey.createTextAttributesKey(
        "RON_IDENT",
        DefaultLanguageHighlighterColors.IDENTIFIER
    )
    val OBJECT_NAME = TextAttributesKey.createTextAttributesKey(
        "RON_OBJECT_NAME",
        TextAttributesKey.createTextAttributesKey("ron.lang.object_name")
    )
    val KEY_NAME = TextAttributesKey.createTextAttributesKey(
        "RON_KEY_NAME",
        DefaultLanguageHighlighterColors.CONSTANT
    )
    val COMMENT = TextAttributesKey.createTextAttributesKey(
        "RON_COMMENT",
        DefaultLanguageHighlighterColors.LINE_COMMENT
    )
    val BAD_CHAR = TextAttributesKey.createTextAttributesKey(
        "RON_BAD_CHAR",
        HighlighterColors.BAD_CHARACTER
    )

    @JvmField
    val COLON_KEYS = arrayOf(COLON)

    @JvmField
    val COMMA_KEYS = arrayOf(COMMA)

    @JvmField
    val PARENTHESES_KEYS = arrayOf(PARENTHESES)

    @JvmField
    val BRACKETS_KEYS = arrayOf(BRACKETS)

    @JvmField
    val BRACES_KEYS = arrayOf(BRACES)

    @JvmField
    val BOOLEAN_KEYS = arrayOf(BOOLEAN)

    @JvmField
    val NUMBER_KEYS = arrayOf(NUMBER)

    @JvmField
    val STRING_KEYS = arrayOf(STRING)

    @JvmField
    val OPTION_KEYS = arrayOf(OPTION)

    @JvmField
    val EXTENSION_KEYS = arrayOf(EXTENSION)

    @JvmField
    val IDENT_KEYS = arrayOf(IDENT)

    @JvmField
    val COMMENT_KEYS = arrayOf(COMMENT)

    @JvmField
    val BAD_CHAR_KEYS = arrayOf(BAD_CHAR)

    @JvmField
    val EMPTY_KEYS = arrayOfNulls<TextAttributesKey>(0)
}

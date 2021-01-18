package com.github.madwareru.intellijronremix.ide.colors

import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighter
import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts
import com.github.madwareru.intellijronremix.ide.icons.RONIcons
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class RONColorSettingsPage : ColorSettingsPage {
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> =
        arrayOf(
            AttributesDescriptor("Colon", RONSyntaxHighlighterConsts.COLON),
            AttributesDescriptor("Comma", RONSyntaxHighlighterConsts.COMMA),
            AttributesDescriptor("Parentheses", RONSyntaxHighlighterConsts.PARENTHESES),
            AttributesDescriptor("Brackets", RONSyntaxHighlighterConsts.BRACKETS),
            AttributesDescriptor("Braces", RONSyntaxHighlighterConsts.BRACES),
            AttributesDescriptor("Boolean", RONSyntaxHighlighterConsts.BOOLEAN),
            AttributesDescriptor("Number", RONSyntaxHighlighterConsts.NUMBER),
            AttributesDescriptor("String", RONSyntaxHighlighterConsts.STRING),
            AttributesDescriptor("Option", RONSyntaxHighlighterConsts.OPTION),
            AttributesDescriptor("Extension", RONSyntaxHighlighterConsts.EXTENSION),
            AttributesDescriptor("Ident", RONSyntaxHighlighterConsts.IDENT),
            AttributesDescriptor("Comment", RONSyntaxHighlighterConsts.COMMENT),
            AttributesDescriptor("Bad character", RONSyntaxHighlighterConsts.BAD_CHAR),
        )

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String = "RON"

    override fun getIcon(): Icon = RONIcons.FILE

    override fun getHighlighter(): SyntaxHighlighter = RONSyntaxHighlighter()

    override fun getDemoText(): String {
        return "(\n" +
            "    rocky_tiles_count: 10,\n" +
            "    randomized_objects: [\n" +
            "        (owner: None, typename: \"boulder\", line: None, count: 3),\n" +
            "        (owner: None, typename: \"spike_trap\", line: None, count: 3),\n" +
            "        (owner: Some((0)), typename: \"swordsman\", line: Some(Front), count: 1),\n" +
            "        (owner: Some((0)), typename: \"hammerman\", line: Some(Front), count: 1),\n" +
            "        (owner: Some((0)), typename: \"spearman\", line: Some(Middle), count: 1),\n" +
            "        (owner: Some((0)), typename: \"alchemist\", line: Some(Middle), count: 1),\n" +
            "        (owner: Some((1)), typename: \"imp\", line: Some(Front), count: 4),\n" +
            "        (owner: Some((1)), typename: \"toxic_imp\", line: Some(Middle), count: 1),\n" +
            "        (owner: Some((1)), typename: \"imp_bomber\", line: Some(Back), count: 1),\n" +
            "        (owner: Some((1)), typename: \"imp_summoner\", line: Some(Middle), count: 2),\n" +
            "    ],\n" +
            ")"
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? {
        return null
    }
}

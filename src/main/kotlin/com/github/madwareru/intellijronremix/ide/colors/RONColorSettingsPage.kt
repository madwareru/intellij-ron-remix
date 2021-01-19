package com.github.madwareru.intellijronremix.ide.colors

import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighter
import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts
import com.github.madwareru.intellijronremix.ide.icons.RONIcons
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class RONColorSettingsPage : ColorSettingsPage {
    private val attrs = arrayOf(
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
        AttributesDescriptor("Identifier", RONSyntaxHighlighterConsts.IDENT),
        AttributesDescriptor("Object name", RONSyntaxHighlighterConsts.OBJECT_NAME),
        AttributesDescriptor("Key", RONSyntaxHighlighterConsts.KEY_NAME),
        AttributesDescriptor("Comment", RONSyntaxHighlighterConsts.COMMENT),
        AttributesDescriptor("Bad character", RONSyntaxHighlighterConsts.BAD_CHAR),
    )

    private val annotatorTags = attrs.associateBy({ it.displayName }, { it.key })

    override fun getAttributeDescriptors() = attrs

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "RON"

    override fun getIcon(): Icon = RONIcons.FILE

    override fun getHighlighter(): SyntaxHighlighter = RONSyntaxHighlighter()

    override fun getDemoText() = "GameConfig( // optional struct name\n" +
        "    window_size: (800, 600),\n" +
        "    window_title: \"PAC-MAN\",\n" +
        "    fullscreen: false,\n" +
        "    \n" +
        "    mouse_sensitivity: 1.4,\n" +
        "    key_bindings: {\n" +
        "        \"up\": Up,\n" +
        "        \"down\": Down,\n" +
        "        \"left\": Left,\n" +
        "        \"right\": Right,\n" +
        "        \n" +
        "        // Uncomment to enable WASD controls\n" +
        "        /*\n" +
        "        \"W\": Up,\n" +
        "        \"A\": Down,\n" +
        "        \"S\": Left,\n" +
        "        \"D\": Right,\n" +
        "        */\n" +
        "    },\n" +
        "    \n" +
        "    difficulty_options: (\n" +
        "        start_difficulty: Easy,\n" +
        "        adaptive: false,\n" +
        "    ),\n" +
        ")"

    override fun getAdditionalHighlightingTagToDescriptorMap() = annotatorTags
}

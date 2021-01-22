package com.github.madwareru.intellijronremix.ide.colors

import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighter
import com.github.madwareru.intellijronremix.ide.icons.RONIcons
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.openapi.util.io.StreamUtil
import org.apache.xerces.impl.io.UTF8Reader

class RONColorSettingsPage : ColorSettingsPage {
    private val attrs = RONColor
        .values()
        .map { it.attributesDescriptor }
        .toTypedArray()

    private val annotatorTags = RONColor
        .values()
        .associateBy({ it.humanName }, { it.textAttributesKey })

    override fun getAttributeDescriptors() = attrs

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName() = "RON"

    override fun getIcon() = RONIcons.FILE

    override fun getHighlighter() = RONSyntaxHighlighter()

    private val _demoText by lazy {
        val stream = javaClass.classLoader.getResourceAsStream("colors/highlighterDemoText.ron")
        val reader = UTF8Reader(stream)
        StreamUtil.convertSeparators(StreamUtil.readText(reader))
    }

    override fun getDemoText() = _demoText

    override fun getAdditionalHighlightingTagToDescriptorMap() = annotatorTags
}

package com.github.madwareru.intellijronremix.ide.codeStyle

import com.github.madwareru.intellijronremix.language.RONLanguage
import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider

class RONLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun getLanguage() = RONLanguage.INSTANCE

    override fun getIndentOptionsEditor() = SmartIndentOptionsEditor()

    override fun createConfigurable(
        settings: CodeStyleSettings,
        modelSettings: CodeStyleSettings
    ): CodeStyleConfigurable {
        return object : CodeStyleAbstractConfigurable(
            settings,
            modelSettings,
            configurableDisplayName
        ) {
            override fun createPanel(settings: CodeStyleSettings?) = RONCodeStyleMainPanel(currentSettings, settings)
        }
    }

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        when (settingsType) {
            SettingsType.COMMENTER_SETTINGS -> {
                consumer.showStandardOptions(
                    CodeStyleSettingsCustomizable.CommenterOption.BLOCK_COMMENT_AT_FIRST_COLUMN.name,
                    CodeStyleSettingsCustomizable.CommenterOption.LINE_COMMENT_ADD_SPACE.name,
                    CodeStyleSettingsCustomizable.CommenterOption.LINE_COMMENT_AT_FIRST_COLUMN.name
                )
            }
            SettingsType.WRAPPING_AND_BRACES_SETTINGS -> {
                consumer.showStandardOptions(
                    CodeStyleSettingsCustomizable.WrappingOrBraceOption.RIGHT_MARGIN.name,
                    CodeStyleSettingsCustomizable.WrappingOrBraceOption.KEEP_LINE_BREAKS.name
                )
            }
            SettingsType.LANGUAGE_SPECIFIC -> {
                consumer.showStandardOptions()
            }
            else -> { }
        }
    }

    override fun customizeDefaults(
        commonSettings: CommonCodeStyleSettings,
        indentOptions: CommonCodeStyleSettings.IndentOptions
    ) {
        commonSettings.RIGHT_MARGIN = 100

        commonSettings.LINE_COMMENT_AT_FIRST_COLUMN = false
        commonSettings.LINE_COMMENT_ADD_SPACE = true
        commonSettings.BLOCK_COMMENT_AT_FIRST_COLUMN = false

        indentOptions.CONTINUATION_INDENT_SIZE = indentOptions.INDENT_SIZE
    }

    override fun getCodeSample(settingsType: SettingsType) = (
"""Scene( // class name is optional
    materials: { // this is a map
        "metal": (
            reflectivity: 1.0,
        ),
        "plastic": (
            reflectivity: 0.5,
        ),
    },
    entities: [ // this is an array
        (
            name: "hero",
            material: "metal",
        ),
        (
            name: "monster",
            material: "plastic",
        ),
    ],
)""").trim()
}

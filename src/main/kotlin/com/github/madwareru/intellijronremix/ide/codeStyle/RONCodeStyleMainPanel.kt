package com.github.madwareru.intellijronremix.ide.codeStyle

import com.github.madwareru.intellijronremix.language.RONLanguage
import com.intellij.application.options.GenerationCodeStylePanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.psi.codeStyle.CodeStyleSettings

class RONCodeStyleMainPanel(currentSettings: CodeStyleSettings?, settings: CodeStyleSettings?) :
    TabbedLanguageCodeStylePanel(
        RONLanguage.INSTANCE,
        currentSettings,
        settings
    ) {
    override fun initTabs(settings: CodeStyleSettings) {
        addIndentOptionsTab(settings)
        addWrappingAndBracesTab(settings)
        addTab(GenerationCodeStylePanel(settings, RONLanguage.INSTANCE))
    }
}

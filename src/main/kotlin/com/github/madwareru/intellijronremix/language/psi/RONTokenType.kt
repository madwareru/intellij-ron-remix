package com.github.madwareru.intellijronremix.language.psi

import com.github.madwareru.intellijronremix.language.RONLanguage
import com.intellij.psi.tree.IElementType

class RONTokenType(debugName: String) : IElementType(debugName, RONLanguage.INSTANCE) {
    override fun toString(): String = "RONTokenType.${super.toString()}"
}
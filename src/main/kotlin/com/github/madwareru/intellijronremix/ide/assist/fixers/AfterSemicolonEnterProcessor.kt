package com.github.madwareru.intellijronremix.ide.assist.fixers

import com.intellij.lang.SmartEnterProcessorWithFixers
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class AfterSemicolonEnterProcessor : SmartEnterProcessorWithFixers.FixEnterProcessor() {
    override fun doEnter(atCaret: PsiElement, file: PsiFile, editor: Editor, modified: Boolean): Boolean {
//        return when (atCaret) {
//            is RsExprStmt,
//            is RsLetDecl -> {
//                val elementEndOffset = atCaret.endOffset
//                editor.caretModel.moveToOffset(elementEndOffset)
//                modified
//            }
//            else -> false
//        }
        return true
    }
}
package com.github.madwareru.intellijronremix.ide.annotator

import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts
import com.github.madwareru.intellijronremix.language.psi.RONNamedField
import com.github.madwareru.intellijronremix.language.psi.RONObject
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement

class RONHighlightingAnnotator : AnnotatorBase() {
    override fun annotateInternal(element: PsiElement, holder: AnnotationHolder) {
        val parent = element.parent
        if (holder.isBatchMode || parent == null) return

        when (parent) {
            is RONObject -> if (parent.ident == element) {
                holder
                    .newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(RONSyntaxHighlighterConsts.OBJECT_NAME)
                    .create()
            }
            is RONNamedField -> if (parent.ident == element) {
                holder
                    .newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(RONSyntaxHighlighterConsts.KEY_NAME)
                    .create()
            }
        }
    }
}

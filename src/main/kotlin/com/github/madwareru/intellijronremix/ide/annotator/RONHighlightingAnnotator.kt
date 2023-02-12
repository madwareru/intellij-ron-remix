package com.github.madwareru.intellijronremix.ide.annotator

import com.github.madwareru.intellijronremix.ide.colors.RONColor
import com.github.madwareru.intellijronremix.language.psi.RONEnum
import com.github.madwareru.intellijronremix.language.psi.RONExt
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
            is RONExt -> if (parent.ident != element) {
                holder
                    .newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(RONColor.EXTENSION.textAttributesKey)
                    .create()
            }
            is RONObject -> if (parent.objectName == element) {
                holder
                    .newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(RONColor.OBJECT_NAME.textAttributesKey)
                    .create()
            }
            is RONEnum -> if (parent.ident == element) {
                holder
                    .newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(RONColor.OBJECT_NAME.textAttributesKey)
                    .create()
            }
            is RONNamedField -> if (parent.fieldName == element) {
                holder
                    .newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(RONColor.KEY_NAME.textAttributesKey)
                    .create()
            }
        }
    }
}

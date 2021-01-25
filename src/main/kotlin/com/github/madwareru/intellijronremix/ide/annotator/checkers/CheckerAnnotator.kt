package com.github.madwareru.intellijronremix.ide.annotator.checkers

import com.github.madwareru.intellijronremix.ide.annotator.AnnotatorBase
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement

abstract class CheckerAnnotator : AnnotatorBase() {
    protected abstract fun check(element: PsiElement, holder: AnnotationHolder): CheckerAnnotatorResult
    override fun annotateInternal(element: PsiElement, holder: AnnotationHolder) {
        when (val result = check(element, holder)) {
            CheckerAnnotatorResult.Ok -> {}
            is CheckerAnnotatorResult.Error -> {
                val (errorText, subRange) = result
                holder
                    .newAnnotation(HighlightSeverity.ERROR, errorText)
                    .range(subRange)
                    .create()
            }
        }
    }
}
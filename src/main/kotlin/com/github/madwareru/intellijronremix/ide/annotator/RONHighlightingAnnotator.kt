package com.github.madwareru.intellijronremix.ide.annotator

import com.github.madwareru.intellijronremix.ide.RONSyntaxHighlighterConsts
import com.github.madwareru.intellijronremix.language.psi.RONElementType
import com.github.madwareru.intellijronremix.language.psi.RONNamedField
import com.github.madwareru.intellijronremix.language.psi.RONObject
import com.intellij.ide.plugins.PluginManagerCore.isUnitTestMode
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiElement
import com.intellij.util.containers.ContainerUtil
import org.jetbrains.annotations.TestOnly

class RONHighlightingAnnotator : AnnotatorBase() {
    override fun annotateInternal(element: PsiElement, holder: AnnotationHolder) {
        if(holder.isBatchMode) return
        if(element.parent == null) return

        when(val parent = element.parent) {
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

abstract class AnnotatorBase : Annotator {

    final override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (!isUnitTestMode || javaClass in enabledAnnotators) {
            annotateInternal(element, holder)
        }
    }

    protected abstract fun annotateInternal(element: PsiElement, holder: AnnotationHolder)

    companion object {
        private val enabledAnnotators: MutableSet<Class<out AnnotatorBase>> = ContainerUtil.newConcurrentSet()

        @TestOnly
        fun enableAnnotator(annotatorClass: Class<out AnnotatorBase>, parentDisposable: Disposable) {
            enabledAnnotators += annotatorClass
            Disposer.register(parentDisposable, Disposable { enabledAnnotators -= annotatorClass })
        }
    }
}
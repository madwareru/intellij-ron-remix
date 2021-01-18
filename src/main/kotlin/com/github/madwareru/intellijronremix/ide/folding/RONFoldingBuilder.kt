package com.github.madwareru.intellijronremix.ide.folding

import com.github.madwareru.intellijronremix.language.psi.RONFile
import com.github.madwareru.intellijronremix.language.psi.RONList
import com.github.madwareru.intellijronremix.language.psi.RONObjectBody
import com.github.madwareru.intellijronremix.language.psi.RONMap
import com.github.madwareru.intellijronremix.language.psi.RONTypes
import com.github.madwareru.intellijronremix.language.psi.RONRecursiveVisitor
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.CustomFoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class RONFoldingBuilder : CustomFoldingBuilder(), DumbAware {
    override fun buildLanguageFoldRegions(descriptors: MutableList<FoldingDescriptor>, root: PsiElement, document: Document, quick: Boolean) {
        if (root !is RONFile) return
        val visitor = RONFoldingVisitor(descriptors)
        PsiTreeUtil.processElements(root) {
            it.accept(visitor); true
        }
    }

    override fun getLanguagePlaceholderText(node: ASTNode, range: TextRange) =
        when (node.elementType) {
            RONTypes.LIST -> "[...]"
            RONTypes.OBJECT_BODY -> "(...)"
            RONTypes.MAP -> "{...}"
            else -> "{...}"
        }

    override fun isRegionCollapsedByDefault(node: ASTNode) = false
}

private class RONFoldingVisitor(private val descriptors: MutableList<FoldingDescriptor>) : RONRecursiveVisitor() {
    override fun visitList(o: RONList) {
        if (o.valueList.isNotEmpty()) {
            fold(o)
            super.visitList(o)
        }
    }

    override fun visitObjectBody(o: RONObjectBody) {
        if (o.objectEntryList.isNotEmpty()) {
            fold(o)
            super.visitObjectBody(o)
        }
    }

    override fun visitMap(o: RONMap) {
        if (o.mapEntryList.isNotEmpty()) {
            fold(o)
            super.visitMap(o)
        }
    }

    private fun fold(element: PsiElement) {
        descriptors += FoldingDescriptor(element.node, element.textRange)
    }
}

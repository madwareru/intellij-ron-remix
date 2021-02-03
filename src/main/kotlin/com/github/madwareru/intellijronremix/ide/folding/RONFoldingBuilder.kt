package com.github.madwareru.intellijronremix.ide.folding

import com.github.madwareru.intellijronremix.language.psi.*
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.CustomFoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class RONFoldingBuilder : CustomFoldingBuilder(), DumbAware {
    override fun buildLanguageFoldRegions(
        descriptors: MutableList<FoldingDescriptor>,
        root: PsiElement,
        document: Document,
        quick: Boolean
    ) {
        if (root !is RONFile) return
        val visitor = RONFoldingVisitor(descriptors)
        PsiTreeUtil.processElements(root) {
            it.accept(visitor); true
        }
    }

    override fun getLanguagePlaceholderText(node: ASTNode, range: TextRange) =
        when (node.elementType) {
            RONTypes.LIST -> "[...]"
            RONTypes.OBJECT_BODY, RONTypes.TUPLE_BODY -> "(...)"
            RONTypes.MAP -> "{...}"
            RONTypes.BLOCK_COMMENT -> "/*...*/"
            RONTypes.EXTENSIONS -> "#![...]"
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

    override fun visitExtensions(o: RONExtensions) {
        fold(o)
        super.visitExtensions(o)
    }

    override fun visitObjectBody(o: RONObjectBody) {
        if (o.objectEntryList.isNotEmpty()) {
            fold(o)
            super.visitObjectBody(o)
        }
    }

    override fun visitTupleBody(o: RONTupleBody) {
        if (o.valueList.isNotEmpty()) {
            fold(o)
            super.visitTupleBody(o)
        }
    }

    override fun visitMap(o: RONMap) {
        if (o.mapEntryList.isNotEmpty()) {
            fold(o)
            super.visitMap(o)
        }
    }

    override fun visitComment(comment: PsiComment) {
        if (comment.tokenType == RONTypes.BLOCK_COMMENT) {
            fold(comment)
            super.visitComment(comment)
        }
    }

    private fun fold(element: PsiElement) {
        descriptors += FoldingDescriptor(element.node, element.textRange)
    }
}

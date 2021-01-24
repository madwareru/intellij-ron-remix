package com.github.madwareru.intellijronremix.ide.formatter

import com.intellij.formatting.Alignment
import com.intellij.formatting.ASTBlock
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.Indent
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange

class RONFormattingModelBuilder : FormattingModelBuilder {
    override fun getRangeAffectingIndent(
        file: com.intellij.psi.PsiFile?,
        offset: Int,
        elementAtOffset: ASTNode?
    ): TextRange? = null

    override fun createModel(
        element: com.intellij.psi.PsiElement,
        settings: com.intellij.psi.codeStyle.CodeStyleSettings
    ): FormattingModel {
        val ctx = RONFormatterContext.create(settings)
        val block = createBlock(element.node, null, Indent.getNoneIndent(), null, ctx)
        return FormattingModelProvider.createFormattingModelForPsiFile(element.containingFile, block, settings)
    }

    companion object {
        fun createBlock(
            node: ASTNode,
            alignment: Alignment?,
            indent: Indent?,
            wrap: Wrap?,
            ctx: RONFormatterContext
        ): ASTBlock = RONAstBlock(node, alignment, indent, wrap, ctx)
    }
}

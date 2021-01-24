package com.github.madwareru.intellijronremix.ide.formatter

import com.github.madwareru.intellijronremix.language.psi.RONTypes
import com.github.madwareru.intellijronremix.language.RONLanguage
import com.intellij.formatting.ASTBlock
import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.formatter.FormatterUtil
import com.intellij.psi.TokenType

class RONAstBlock(
    private val node: ASTNode,
    private val alignment: Alignment?,
    private val indent: Indent?,
    private val wrap: Wrap?,
    val ctx: RONFormatterContext
) : ASTBlock {
    override fun isLeaf(): Boolean = node.firstChildNode == null

    override fun getNode() = node

    override fun getTextRange(): TextRange = node.textRange

    override fun getWrap() = wrap

    override fun getIndent() = indent

    override fun getAlignment() = alignment

    override fun getSpacing(child1: Block?, child2: Block) = computeSpacing(child1, child2, ctx)

    override fun getSubBlocks(): List<Block> = mySubBlocks

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val indent = when (node.elementType) {
            RONTypes.OBJECT_BODY, RONTypes.MAP, RONTypes.LIST -> Indent.getNormalIndent()
            else -> Indent.getNoneIndent()
        }
        return ChildAttributes(indent, null)
    }

    override fun isIncomplete(): Boolean = myIsIncomplete

    private val myIsIncomplete: Boolean by lazy { FormatterUtil.isIncomplete(node) }

    private val mySubBlocks: List<Block> by lazy { buildChildren() }
}

data class RONFormatterContext(
    val commonSettings: CommonCodeStyleSettings,
    val spacingBuilder: SpacingBuilder
) {
    companion object {
        fun create(settings: CodeStyleSettings): RONFormatterContext {
            val commonSettings = settings.getCommonSettings(RONLanguage.INSTANCE)
            return RONFormatterContext(commonSettings, createSpacingBuilder(commonSettings))
        }
    }
}

fun createSpacingBuilder(commonSettings: CommonCodeStyleSettings): SpacingBuilder =
    SpacingBuilder(commonSettings)
        // ,
        .after(RONTypes.COMMA).spacing(1, 1, 0, true, 0)
        .before(RONTypes.COMMA).spaceIf(false)
        // [ ]
        .after(RONTypes.BRACKETL).spaceIf(false)
        .before(RONTypes.BRACKETR).spaceIf(false)
        // { }
        .after(RONTypes.BRACEL).spaceIf(true)
        .before(RONTypes.BRACER).spaceIf(true)
        // ( )
        .after(RONTypes.PARENTHESISL).spaceIf(true)
        .before(RONTypes.PARENTHESISR).spaceIf(true)

private fun Block.computeSpacing(child1: Block?, child2: Block, ctx: RONFormatterContext): Spacing? {
    return ctx.spacingBuilder.getSpacing(this, child1, child2)
}

private fun ASTNode?.isWhitespaceOrEmpty() = this == null || textLength == 0 || elementType == TokenType.WHITE_SPACE

private fun RONAstBlock.computeIndent(child: ASTNode): Indent? = when (node.elementType) {
    RONTypes.OBJECT_BODY, RONTypes.MAP, RONTypes.LIST -> when (child.elementType) {
        RONTypes.COMMA -> Indent.getNoneIndent()
        else -> Indent.getNormalIndent()
    }
    else -> Indent.getNoneIndent()
}

private fun RONAstBlock.buildChildren(): List<Block> {
    return node.getChildren(null)
        .filter { !it.isWhitespaceOrEmpty() }
        .map { childNode ->
            RONFormattingModelBuilder.createBlock(
                node = childNode,
                alignment = null,
                indent = computeIndent(childNode),
                wrap = null,
                ctx
            )
        }
}

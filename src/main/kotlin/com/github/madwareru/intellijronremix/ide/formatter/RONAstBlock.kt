package com.github.madwareru.intellijronremix.ide.formatter

import com.github.madwareru.intellijronremix.language.RONLanguage
import com.github.madwareru.intellijronremix.language.psi.RONTypes
import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.formatter.FormatterUtil

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

    private val myIsIncomplete: Boolean by lazy {
        node.getChildren(null).any {
            it.elementType is PsiErrorElement
        }
        || FormatterUtil.isIncomplete(node)
    }

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
        .after(RONTypes.BRACEL).spaceIf(false)
        .before(RONTypes.BRACER).spaceIf(false)
        // ( )
        .after(RONTypes.PARENTHESISL).spaceIf(false)
        .before(RONTypes.PARENTHESISR).spaceIf(false)

private fun Block.computeSpacing(child1: Block?, child2: Block, ctx: RONFormatterContext): Spacing? {
    return ctx.spacingBuilder.getSpacing(this, child1, child2)
}

private fun ASTNode?.isWhitespaceOrEmpty() = this == null || textLength == 0 || elementType == TokenType.WHITE_SPACE

private fun RONAstBlock.computeIndent(child: ASTNode): Indent? {
    return when (node.elementType) {
        RONTypes.OBJECT_BODY -> when (child.elementType) {
            RONTypes.COMMA, RONTypes.PARENTHESISL, RONTypes.PARENTHESISR -> Indent.getNoneIndent()
            else -> Indent.getNormalIndent()
        }
        RONTypes.MAP -> when (child.elementType) {
            RONTypes.COMMA, RONTypes.BRACEL, RONTypes.BRACER -> Indent.getNoneIndent()
            else -> Indent.getNormalIndent()
        }
        RONTypes.LIST -> when (child.elementType) {
            RONTypes.COMMA, RONTypes.BRACKETL, RONTypes.BRACKETR -> Indent.getNoneIndent()
            else -> Indent.getNormalIndent()
        }
        else -> Indent.getNoneIndent()
    }
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

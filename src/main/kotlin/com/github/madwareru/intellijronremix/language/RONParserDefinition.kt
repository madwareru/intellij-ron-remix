package com.github.madwareru.intellijronremix.language

import com.github.madwareru.intellijronremix.language.parser._RONParser
import com.github.madwareru.intellijronremix.language.psi.RONFile
import com.github.madwareru.intellijronremix.language.psi.RONTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class RONParserDefinition : ParserDefinition {
    override fun createLexer(project: Project): Lexer = RONLexerAdapter()

    override fun createParser(project: Project): PsiParser = _RONParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = STRING_LITERALS

    override fun createElement(node: ASTNode): PsiElement = RONTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = RONFile(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements =
        ParserDefinition.SpaceRequirements.MAY

    companion object {
        val COMMENTS = TokenSet.create(RONTypes.COMMENT, RONTypes.BLOCK_COMMENT)
        val STRING_LITERALS = TokenSet.create(RONTypes.STRING, RONTypes.RAW_STRING)
        val FILE = IFileElementType(RONLanguage.INSTANCE)
    }
}

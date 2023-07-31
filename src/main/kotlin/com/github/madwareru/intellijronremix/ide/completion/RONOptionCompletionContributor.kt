package com.github.madwareru.intellijronremix.ide.completion

import com.github.madwareru.intellijronremix.language.psi.RONExt
import com.github.madwareru.intellijronremix.language.psi.RONExtensions
import com.github.madwareru.intellijronremix.language.psi.RONObjectName
import com.github.madwareru.intellijronremix.language.psi.RONTypes
import com.github.madwareru.intellijronremix.language.psi.RONValue
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext

class RONOptionCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(RONTypes.IDENT),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    resultSet: CompletionResultSet
                ) {
                    if (parameters.position.parentOfType<RONExt>(false) != null) {
                        resultSet.addElement(
                            LookupElementBuilder
                                .create("enable()")
                                .withInsertHandler { ctx, _ ->
                                    EditorModificationUtil.moveCaretRelatively(ctx.editor, -1)
                                }
                        )
                    }
                    if (parameters.position.parent is RONObjectName && parameters.position.parent.parent is RONValue) {
                        resultSet.addElement(
                            LookupElementBuilder
                                .create("Some()")
                                .withInsertHandler { ctx, _ ->
                                    EditorModificationUtil.moveCaretRelatively(ctx.editor, - 1)
                                }
                        )
                        resultSet.addElement(LookupElementBuilder.create("None"))
                    }
                }
            }
        )
    }
}
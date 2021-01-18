package com.github.madwareru.intellijronremix.ide.actions

import com.github.madwareru.intellijronremix.ide.icons.RONIcons
import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

class RONCreateFile : CreateFileFromTemplateAction(
    NAME,
    "Create new RON file",
    RONIcons.FILE
) {
    override fun buildDialog(
        project: Project,
        directory: PsiDirectory,
        builder: CreateFileFromTemplateDialog.Builder
    ) {
        builder
            .setTitle(NAME)
            .addKind("Empty file", RONIcons.FILE, "RON File")
    }

    override fun getActionName(
        directory: PsiDirectory,
        newName: String,
        templateName: String
    ): String = NAME

    companion object {
        private const val NAME = "RON File"
    }
}
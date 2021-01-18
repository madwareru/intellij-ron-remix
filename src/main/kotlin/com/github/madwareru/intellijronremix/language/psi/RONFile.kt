package com.github.madwareru.intellijronremix.language.psi

import com.github.madwareru.intellijronremix.language.RONFileType
import com.github.madwareru.intellijronremix.language.RONLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class RONFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, RONLanguage.INSTANCE) {
    override fun getFileType(): FileType = RONFileType.INSTANCE

    override fun toString(): String = "RON File"
}
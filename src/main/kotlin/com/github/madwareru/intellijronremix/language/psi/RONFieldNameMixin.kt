package com.github.madwareru.intellijronremix.language.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry

abstract class RONFieldNameMixin(node: ASTNode): ASTWrapperPsiElement(node), RONFieldName {
    override fun getReference(): PsiReference? {
        val references = references
        return if (references.size == 1) references[0] else null
    }

    override fun getReferences(): Array<PsiReference> {
        return ReferenceProvidersRegistry.getReferencesFromProviders(this)
    }
}
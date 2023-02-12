package com.github.madwareru.intellijronremix.rust

import com.github.madwareru.intellijronremix.language.psi.RONObjectName
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

// We use a reference contributor, to avoid adding references in case rust is not installed.
// Also, this allows an external plugin to integrate RON support with other languages.
class RustReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(RONObjectName::class.java), RustReferenceProvider)
    }
}
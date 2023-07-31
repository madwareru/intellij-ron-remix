package com.github.madwareru.intellijronremix.rust

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReferenceBase

abstract class RonToRustReferenceBase<T : PsiElement>(element: T) : PsiPolyVariantReferenceBase<T>(element) {
    override fun isReferenceTo(element: PsiElement) =
        try { multiResolve(false) }
        catch (e: Exception) { e.printStackTrace(); throw e }
        .any { element.manager.areElementsEquivalent(it.element, element) }
}
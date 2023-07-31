package com.github.madwareru.intellijronremix.rust

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveResult
import com.intellij.util.containers.map2Array
import org.rust.lang.core.psi.rustStructureModificationTracker

class RonResolveResult(
    private val resolvedElement: PsiElement,
    private val rustStructureModificationCount: Long
): ResolveResult {
    override fun getElement() = resolvedElement
    override fun isValidResult() = element
        .project
        .rustStructureModificationTracker
        .modificationCount
        .let { it == rustStructureModificationCount }

    companion object {
        fun createResults(
            elements: Collection<PsiElement>,
            rustStructureModificationCount: Long
        ): Array<ResolveResult> {
            return elements.map2Array { RonResolveResult(it, rustStructureModificationCount) }
        }
    }
}
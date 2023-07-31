package com.github.madwareru.intellijronremix.rust

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveResult
import com.intellij.psi.impl.source.resolve.ResolveCache
import org.rust.lang.core.psi.ext.RsNamedElement
import org.rust.lang.core.psi.rustStructureModificationTracker

abstract class RonToRustReferenceCached<T : PsiElement>(element: T) : RonToRustReferenceBase<T>(element) {
    abstract fun resolveInner(): Collection<RsNamedElement>

    override fun resolve(): PsiElement? = multiResolve(false)
        .singleOrNull()
        ?.element

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> = ResolveCache
        .getInstance(element.project)
        .resolveWithCaching(this, Resolver, true, false)
        ?: emptyArray()

    private object Resolver : ResolveCache.AbstractResolver<RonToRustReferenceCached<*>, Array<ResolveResult>> {
        override fun resolve(ref: RonToRustReferenceCached<*>, incompleteCode: Boolean): Array<ResolveResult> {
            // modCount must be determined before the resolve, to avoid wrong results,
            // if it changes between resolve and createResults
            val modCount = ref
                .element
                .project
                .rustStructureModificationTracker
                .modificationCount

            val elements = ref.resolveInner()

            return RonResolveResult.createResults(elements, modCount)
        }
    }
}
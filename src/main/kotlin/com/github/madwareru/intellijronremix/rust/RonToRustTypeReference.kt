package com.github.madwareru.intellijronremix.rust

import com.github.madwareru.intellijronremix.language.psi.RONObjectName
import com.intellij.openapi.util.TextRange
import com.intellij.psi.search.GlobalSearchScope
import org.rust.lang.core.psi.ext.RsNamedElement
import org.rust.lang.core.stubs.index.RsGotoClassIndex
import org.rust.openapiext.getElements

class RonToRustTypeReference(ronObjectName: RONObjectName) : RonToRustReferenceCached<RONObjectName>(ronObjectName) {
    override fun calculateDefaultRangeInElement(): TextRange {
        return TextRange(0, element.textLength)
    }

    // if existing, we assume a type from your own project is meant, even if a type of the same name exists in other projects
    override fun resolveInner(): Collection<RsNamedElement> {
        val inProject = getElements(
            RsGotoClassIndex.KEY,
            element.text,
            element.project,
            GlobalSearchScope.projectScope(element.project)
        )
        if (inProject.isNotEmpty()) return inProject
        return getElements(
            RsGotoClassIndex.KEY,
            element.text,
            element.project,
            GlobalSearchScope.allScope(element.project)
        )
    }
}

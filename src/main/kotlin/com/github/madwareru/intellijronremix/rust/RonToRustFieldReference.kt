package com.github.madwareru.intellijronremix.rust

import com.github.madwareru.intellijronremix.language.psi.RONFieldName
import com.intellij.openapi.util.TextRange
import org.rust.lang.core.psi.ext.RsNamedElement

class RonToRustFieldReference(ronFieldName: RONFieldName) : RonToRustReferenceCached<RONFieldName>(ronFieldName) {
    override fun calculateDefaultRangeInElement(): TextRange {
        return TextRange(0, element.textLength)
    }

    // if existing, we assume a type from your own project is meant, even if a type of the same name exists in other projects
    override fun resolveInner(): Collection<RsNamedElement> {
        return element.inference.possibleFields
    }
}

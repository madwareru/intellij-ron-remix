package com.github.madwareru.intellijronremix

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import me.ffl.intellijDirectoryTests.DirectoryTestConfig
import me.ffl.intellijDirectoryTests.DirectoryTests
import org.rust.lang.core.psi.ext.RsFieldDecl
import org.rust.lang.core.psi.ext.RsFieldsOwner
import org.rust.lang.core.psi.ext.RsQualifiedNamedElement
import org.rust.lang.core.psi.ext.qualifiedName
import kotlin.io.path.Path

private fun PsiElement.findRustPath(): String? {
    if (this is RsFieldDecl) {
        val ownerPath = this.parentOfType<RsFieldsOwner>(true)!!.qualifiedName
        val name = this.name!!
        return "$ownerPath::$name"
    }
    if (this is RsQualifiedNamedElement) {
        return this.qualifiedName
    }
    return null
}

@Suppress("unused")
class RONDirectoryTest: DirectoryTests(
    DirectoryTestConfig.default.run {
        copy(
            testDataPath = Path("src/test/data"),
            // required because there were issues,
            // where intellij-rust's resolve complained about non-backed stubs
            needsHeavyTestRunner = setOf("completion"),
            externalReferenceToString = { it.findRustPath() },
            projectDescriptor = WithStdlibRustProjectDescriptor
        )
    }
)
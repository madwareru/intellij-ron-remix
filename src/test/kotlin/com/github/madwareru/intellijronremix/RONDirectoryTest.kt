package com.github.madwareru.intellijronremix

import me.ffl.intellijDirectoryTests.DirectoryTestConfig
import me.ffl.intellijDirectoryTests.DirectoryTests
import org.rust.lang.core.psi.ext.RsQualifiedNamedElement
import org.rust.lang.core.psi.ext.qualifiedName
import kotlin.io.path.Path

@Suppress("unused")
class RONDirectoryTest: DirectoryTests(
    DirectoryTestConfig.default.run {
        copy(
            testDataPath = Path("src/test/data"),
            externalReferenceToString = { (it as? RsQualifiedNamedElement)?.qualifiedName },
            projectDescriptor = WithStdlibRustProjectDescriptor
        )
    }
)
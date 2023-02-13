package com.github.madwareru.intellijronremix

import me.ffl.intellijDirectoryTests.DirectoryTestConfig
import me.ffl.intellijDirectoryTests.DirectoryTests
import kotlin.io.path.Path

@Suppress("unused")
class RONDirectoryTest: DirectoryTests(
    DirectoryTestConfig.default.run {
        copy(testDataPath = Path("src/test/data"))
    }
)
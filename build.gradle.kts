import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.8.20"
    // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "1.15.0"
    // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "2.0.0"
    // see https://plugins.jetbrains.com/docs/intellij/tools-gradle-grammar-kit-plugin.html
    id("org.jetbrains.grammarkit") version "2022.3.1"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

// Configure project's dependencies
repositories {
    mavenCentral()
}

// Set the JVM language level used to build project. Use Java 11 for 2020.3+, and Java 17 for 2022.2+.
kotlin {
    jvmToolchain (17)
}

sourceSets["main"].java.srcDirs("src/main/gen")

// Configure gradle-intellij-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}

changelog {
    version.set(properties("pluginVersion"))
    groups.set(emptyList())
}

dependencies {
    testImplementation("io.github.flash-freezing-lava", "intellij-directory-tests", "0.2.1")
}

tasks.buildSearchableOptions {
    enabled = false
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

val generateRonLexer = task<GenerateLexerTask>("generateRonLexer") {
    // source flex file
    sourceFile.set(file("src/main/kotlin/com/github/madwareru/intellijronremix/language/__RONLexer.flex"))

    // target directory for lexer
    targetDir.set("src/main/gen/com/github/madwareru/intellijronremix/language/")

    // target classname, target file will be targetDir/targetClass.java
    targetClass.set("__RONLexer")

    // if set, plugin will remove a lexer output file before generating new one. Default: false
    purgeOldFiles.set(true)
}

val generateRonParser = task<GenerateParserTask>("generateRonParser") {
    dependsOn(generateRonLexer)
    sourceFile.set(file("src/main/kotlin/com/github/madwareru/intellijronremix/language/RON.bnf"))
    targetRoot.set("src/main/gen")
    pathToParser.set("/com/github/madwareru/intellijronremix/language/parser/_RONParser.java")
    pathToPsiRoot.set("/com/github/madwareru/intellijronremix/language/psi")
    purgeOldFiles.set(true)
}

tasks {
    test {
        useJUnitPlatform()
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "17"
        dependsOn(generateRonParser)
    }

    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
            projectDir.resolve("README.md")
                .readText()
                .lines()
                .run {
                    val start = "<!-- Plugin description -->"
                    val end = "<!-- Plugin description end -->"

                    if (!containsAll(listOf(start, end))) {
                        throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                    }
                    subList(indexOf(start) + 1, indexOf(end))
                }.joinToString("\n")
                .run { markdownToHTML(this) }
        )

        // Get the latest available change notes from the changelog file
        changeNotes.set(
            provider {
                val changeLogText = changelog.run { renderItem(getLatest()) }

                val fullLog = "[Full Changelog](https://github.com/madwareru/intellij-ron-remix/blob/main/CHANGELOG.md)"

                markdownToHTML("$changeLogText\n\n$fullLog")
            }
        )
    }

    // Configure UI tests plugin
    // Read more: https://github.com/JetBrains/intellij-ui-test-robot
    runIdeForUiTests {
        systemProperty("robot-server.port", "8082")
        systemProperty("ide.mac.message.dialogs.as.sheets", "false")
        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
        systemProperty("jb.consents.confirmation.enabled", "false")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token.set(System.getenv("PUBLISH_TOKEN"))
        // pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels.set(listOf(properties("pluginVersion").split('-').getOrElse(1) { "default" }.split('.').first()))
    }
}

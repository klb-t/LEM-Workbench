package com.example

import org.junit.Assert.assertFalse
import org.junit.Test
import java.io.File

class RealOnlyPolicyTest {
    @Test
    fun productionSourceContainsNoKnownStubMarkers() {
        val sourceRoot = File("src/main/java")
        val forbidden = listOf(
            Regex("NOT\\s+IMPLEMENTED"),
            Regex("[Ff]ake[ _-]?[Mm]etrics"),
            Regex("PlaceholderScreen"),
            Regex("\\bTODO\\b"),
            Regex("\\bFIXME\\b")
        )
        val hits = sourceRoot.walkTopDown()
            .filter { it.isFile && (it.extension == "kt" || it.extension == "java") }
            .flatMap { file ->
                val text = file.readText()
                forbidden.asSequence()
                    .filter { regex -> regex.containsMatchIn(text) }
                    .map { regex -> "${file.path}: ${regex.pattern}" }
            }
            .toList()

        assertFalse("Production stub markers found: ${hits.joinToString()}", hits.isNotEmpty())
    }
}

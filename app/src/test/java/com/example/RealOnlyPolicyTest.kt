package com.example

import org.junit.Assert.assertFalse
import org.junit.Test
import java.io.File

class RealOnlyPolicyTest {
    @Test
    fun productionSourceContainsNoKnownStubMarkers() {
        val sourceRoot = File("src/main/java")
        val forbidden = listOf(
            "NOT IMPLEMENTED",
            "fake metrics",
            "PlaceholderScreen",
            "TODO",
            "FIXME"
        )
        val hits = sourceRoot.walkTopDown()
            .filter { it.isFile && (it.extension == "kt" || it.extension == "java") }
            .flatMap { file ->
                val text = file.readText()
                forbidden.asSequence()
                    .filter { marker -> text.contains(marker, ignoreCase = true) }
                    .map { marker -> "${file.path}: $marker" }
            }
            .toList()

        assertFalse("Production stub markers found: ${hits.joinToString()}", hits.isNotEmpty())
    }
}

package com.example

import com.example.api.Content
import com.example.api.EmbedContentRequest
import com.example.api.GenerateContentRequest
import com.example.api.GenerationConfig
import com.example.api.Part
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertTrue
import org.junit.Test

class GeminiApiSerializationTest {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Test
    fun generateContentRequestSerializesThroughProductionMoshiPath() {
        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = "ping")))),
            generationConfig = GenerationConfig(temperature = 0f),
            systemInstruction = Content(parts = listOf(Part(text = "system")))
        )

        val json = moshi.adapter(GenerateContentRequest::class.java).toJson(request)

        assertTrue(json.contains("\"contents\""))
        assertTrue(json.contains("\"generationConfig\""))
        assertTrue(json.contains("\"systemInstruction\""))
        assertTrue(json.contains("\"ping\""))
    }

    @Test
    fun embedContentRequestSerializesThroughProductionMoshiPath() {
        val request = EmbedContentRequest(
            model = "models/gemini-embedding-2-preview",
            content = Content(parts = listOf(Part(text = "semantic text"))),
            outputDimensionality = 768
        )

        val json = moshi.adapter(EmbedContentRequest::class.java).toJson(request)

        assertTrue(json.contains("\"model\""))
        assertTrue(json.contains("\"content\""))
        assertTrue(json.contains("\"outputDimensionality\":768"))
    }
}

package com.example.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResearchAgent {

    private val apiService = RetrofitClient.service

    private fun apiKey(): String = ApiKeyStore.getGeminiApiKey()

    suspend fun generateText(
        prompt: String,
        modelName: String = "gemini-3.5-flash",
        systemPrompt: String? = null
    ): String? = withContext(Dispatchers.IO) {
        val key = apiKey()
        if (key.isEmpty()) return@withContext "API Key missing"

        val systemInstruction = systemPrompt?.let {
            Content(parts = listOf(Part(text = it)))
        }

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            systemInstruction = systemInstruction
        )
        try {
            val response = apiService.generateContent(modelName, key, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    suspend fun getEmbedding(
        text: String,
        modelName: String = "gemini-embedding-2-preview",
        dimensions: Int? = null
    ): List<Float>? = withContext(Dispatchers.IO) {
        val key = apiKey()
        if (key.isEmpty()) return@withContext null

        val request = EmbedContentRequest(
            model = "models/$modelName",
            content = Content(parts = listOf(Part(text = text))),
            outputDimensionality = dimensions
        )
        try {
            val response = apiService.embedContent(modelName, key, request)
            response.embedding.values
        } catch (_: Exception) {
            null
        }
    }

    suspend fun listModels(): List<ApiModel> = withContext(Dispatchers.IO) {
        val key = apiKey()
        if (key.isEmpty()) return@withContext emptyList()
        try {
            apiService.listModels(key).models.orEmpty()
        } catch (_: Exception) {
            emptyList()
        }
    }
}

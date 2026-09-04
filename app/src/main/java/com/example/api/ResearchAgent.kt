package com.example.api

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResearchAgent {

    private val apiKey = BuildConfig.GEMINI_API_KEY
    private val apiService = RetrofitClient.service

    suspend fun generateText(
        prompt: String,
        modelName: String = "gemini-3.5-flash",
        systemPrompt: String? = null
    ): String? = withContext(Dispatchers.IO) {
        if (apiKey.isEmpty()) return@withContext "API Key missing"

        val systemInstruction = systemPrompt?.let {
            Content(parts = listOf(Part(text = it)))
        }

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            systemInstruction = systemInstruction
        )
        try {
            val response = apiService.generateContent(modelName, apiKey, request)
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
        if (apiKey.isEmpty()) return@withContext null

        val request = EmbedContentRequest(
            model = "models/$modelName",
            content = Content(parts = listOf(Part(text = text))),
            outputDimensionality = dimensions
        )
        try {
            val response = apiService.embedContent(modelName, apiKey, request)
            response.embedding.values
        } catch (e: Exception) {
            null
        }
    }

    suspend fun listModels(): List<ApiModel> = withContext(Dispatchers.IO) {
        if (apiKey.isEmpty()) return@withContext emptyList()
        try {
            apiService.listModels(apiKey).models.orEmpty()
        } catch (_: Exception) {
            emptyList()
        }
    }
}

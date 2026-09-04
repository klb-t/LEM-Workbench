package com.example.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResearchAgent {

    private val apiService = RetrofitClient.service

    private fun requireApiKey(): String = ApiKeyStore.getGeminiApiKey().ifBlank {
        throw IllegalStateException("Gemini API key is not configured")
    }

    private fun modelId(modelName: String): String = modelName.removePrefix("models/")

    suspend fun generateText(
        prompt: String,
        modelName: String,
        systemPrompt: String? = null
    ): String = withContext(Dispatchers.IO) {
        val key = requireApiKey()
        val id = modelId(modelName)
        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            generationConfig = GenerationConfig(temperature = 0f),
            systemInstruction = systemPrompt?.let { Content(parts = listOf(Part(text = it))) }
        )
        val response = apiService.generateContent(id, key, request)
        response.candidates
            ?.firstOrNull()
            ?.content
            ?.parts
            ?.mapNotNull { it.text }
            ?.joinToString("")
            ?.takeIf { it.isNotBlank() }
            ?: throw IllegalStateException("Gemini generation returned no text")
    }

    suspend fun getEmbedding(
        text: String,
        modelName: String,
        dimensions: Int? = null
    ): List<Float> = withContext(Dispatchers.IO) {
        val key = requireApiKey()
        val id = modelId(modelName)
        val request = EmbedContentRequest(
            model = "models/$id",
            content = Content(parts = listOf(Part(text = text))),
            outputDimensionality = dimensions
        )
        apiService.embedContent(id, key, request).embedding.values
            .takeIf { it.isNotEmpty() }
            ?: throw IllegalStateException("Gemini embedding returned an empty vector")
    }

    suspend fun listModels(): List<ApiModel> = withContext(Dispatchers.IO) {
        apiService.listModels(requireApiKey()).models.orEmpty()
    }
}

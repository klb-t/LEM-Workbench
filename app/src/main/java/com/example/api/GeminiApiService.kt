package com.example.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class GenerateContentRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig? = null,
    val systemInstruction: Content? = null
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String? = null
)

data class GenerationConfig(
    val temperature: Float? = null,
    val topP: Float? = null,
    val topK: Int? = null,
    val responseMimeType: String? = null
)

data class GenerateContentResponse(
    val candidates: List<Candidate>?
)

data class Candidate(
    val content: Content?
)

data class EmbedContentRequest(
    val model: String? = null,
    val content: Content,
    val taskType: String? = null,
    val outputDimensionality: Int? = null
)

data class EmbedContentResponse(
    val embedding: Embedding
)

data class Embedding(
    val values: List<Float>
)

data class ApiModel(
    val name: String? = null,
    val displayName: String? = null,
    val description: String? = null,
    val supportedGenerationMethods: List<String>? = null,
    val inputTokenLimit: Int? = null,
    val outputTokenLimit: Int? = null
)

data class ListModelsResponse(
    val models: List<ApiModel>? = null,
    val nextPageToken: String? = null
)

interface GeminiApiService {
    @GET("v1beta/models")
    suspend fun listModels(
        @Query("key") apiKey: String,
        @Query("pageSize") pageSize: Int = 1000
    ): ListModelsResponse

    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse

    @POST("v1beta/models/{model}:embedContent")
    suspend fun embedContent(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body request: EmbedContentRequest
    ): EmbedContentResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    val service: GeminiApiService by lazy {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }
}

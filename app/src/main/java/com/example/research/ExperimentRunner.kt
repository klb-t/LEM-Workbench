package com.example.research

import com.example.api.ResearchAgent
import com.example.data.model.ExperimentResult
import java.security.MessageDigest
import java.util.UUID
import kotlin.math.sqrt

/** Real instrument checks only. Every returned record is based on an actual API call. */
class ExperimentRunner(
    private val agent: ResearchAgent = ResearchAgent()
) {
    suspend fun runEncoderSmokeTest(
        modelName: String,
        requestedDimensions: Int = 768
    ): ExperimentResult {
        val experimentId = UUID.randomUUID().toString()
        val started = System.currentTimeMillis()
        val textA = "Safety matters because it protects people from preventable harm."
        val textB = "Preventing avoidable harm is an important reason to protect human safety."

        return try {
            val embeddingA = agent.getEmbedding(textA, modelName, requestedDimensions)
            val embeddingB = agent.getEmbedding(textB, modelName, requestedDimensions)
            require(embeddingA.size == embeddingB.size) { "Embedding dimensions differ" }
            val latencyMs = System.currentTimeMillis() - started
            val cosine = cosineSimilarity(embeddingA, embeddingB)
            val metrics = "{\"success\":true,\"latencyMs\":$latencyMs," +
                "\"actualEmbeddingDimension\":${embeddingA.size}," +
                "\"paraphraseCosineSimilarity\":$cosine}"

            ExperimentResult(
                experimentId = experimentId,
                parentExperimentId = null,
                timestamp = System.currentTimeMillis(),
                hypothesis = "The selected Gemini embedding endpoint is operational.",
                testedConsequence = "Two real API embedding calls return equal, non-empty vectors and a finite paraphrase cosine.",
                corpus = "built-in-instrument-check",
                exactSplit = "n/a",
                embeddingModel = modelName,
                generationModel = "none",
                apiVersion = "v1beta",
                embeddingDimensionality = embeddingA.size,
                taskType = "encoder-instrument-check",
                architecture = "frozen-encoder",
                fullHyperparameters = "{\"requestedDimension\":$requestedDimensions}",
                randomSeed = 0,
                numberOfExamples = 2,
                rawMetrics = metrics,
                perSeedMetrics = "[]",
                confusionMatrices = "[]",
                nullPermutationResults = "[]",
                sourceProvenanceProbes = "[]",
                costTokenCounts = "{}",
                modelProducedInterpretation = "Actual Gemini embedding endpoint call completed.",
                adversarialCritique = "This checks the instrument only; it is not evidence for a LEM hypothesis.",
                status = "INSTRUMENT_OK",
                filesArtifactsProduced = "[]"
            )
        } catch (e: Exception) {
            failedInstrumentResult(
                experimentId = experimentId,
                modelName = modelName,
                kind = "encoder",
                latencyMs = System.currentTimeMillis() - started,
                error = e
            )
        }
    }

    suspend fun runGenerationSmokeTest(modelName: String): ExperimentResult {
        val experimentId = UUID.randomUUID().toString()
        val started = System.currentTimeMillis()
        val contract = "LEM_LAB_GENERATION_OK"

        return try {
            val response = agent.generateText(
                prompt = "Return exactly this token and nothing else: $contract",
                modelName = modelName,
                systemPrompt = "This is an API instrument check. Follow the requested exact-output contract."
            )
            val latencyMs = System.currentTimeMillis() - started
            val normalized = response.trim()
            val contractMatched = normalized == contract
            val digest = sha256(normalized)
            val metrics = "{\"success\":true,\"latencyMs\":$latencyMs," +
                "\"responseChars\":${normalized.length}," +
                "\"contractMatched\":$contractMatched," +
                "\"responseSha256\":\"$digest\"}"

            ExperimentResult(
                experimentId = experimentId,
                parentExperimentId = null,
                timestamp = System.currentTimeMillis(),
                hypothesis = "The selected Gemini generation endpoint is operational and serializes requests correctly.",
                testedConsequence = "A real generateContent request returns non-empty text through the production Retrofit/Moshi path.",
                corpus = "built-in-instrument-check",
                exactSplit = "n/a",
                embeddingModel = "none",
                generationModel = modelName,
                apiVersion = "v1beta",
                embeddingDimensionality = 0,
                taskType = "generation-instrument-check",
                architecture = "direct-generation-endpoint",
                fullHyperparameters = "{\"temperature\":0.0}",
                randomSeed = 0,
                numberOfExamples = 1,
                rawMetrics = metrics,
                perSeedMetrics = "[]",
                confusionMatrices = "[]",
                nullPermutationResults = "[]",
                sourceProvenanceProbes = "[]",
                costTokenCounts = "{}",
                modelProducedInterpretation = "Actual Gemini generateContent endpoint call completed.",
                adversarialCritique = if (contractMatched) {
                    "The exact-output contract passed; this still establishes API operation only."
                } else {
                    "The endpoint returned text but did not satisfy the exact-output contract; API operation is verified, instruction compliance is not."
                },
                status = if (contractMatched) "INSTRUMENT_OK" else "INSTRUMENT_PARTIAL",
                filesArtifactsProduced = "[]"
            )
        } catch (e: Exception) {
            failedInstrumentResult(
                experimentId = experimentId,
                modelName = modelName,
                kind = "generation",
                latencyMs = System.currentTimeMillis() - started,
                error = e
            )
        }
    }

    private fun failedInstrumentResult(
        experimentId: String,
        modelName: String,
        kind: String,
        latencyMs: Long,
        error: Exception
    ): ExperimentResult {
        val message = error.message ?: error::class.java.simpleName
        val metrics = "{\"success\":false,\"latencyMs\":$latencyMs," +
            "\"error\":\"${jsonEscape(message)}\"}"
        return ExperimentResult(
            experimentId = experimentId,
            parentExperimentId = null,
            timestamp = System.currentTimeMillis(),
            hypothesis = "The selected Gemini $kind endpoint is operational.",
            testedConsequence = "A real production API request should complete successfully.",
            corpus = "built-in-instrument-check",
            exactSplit = "n/a",
            embeddingModel = if (kind == "encoder") modelName else "none",
            generationModel = if (kind == "generation") modelName else "none",
            apiVersion = "v1beta",
            embeddingDimensionality = 0,
            taskType = "$kind-instrument-check",
            architecture = "production-api-path",
            fullHyperparameters = "{}",
            randomSeed = 0,
            numberOfExamples = 1,
            rawMetrics = metrics,
            perSeedMetrics = "[]",
            confusionMatrices = "[]",
            nullPermutationResults = "[]",
            sourceProvenanceProbes = "[]",
            costTokenCounts = "{}",
            modelProducedInterpretation = "The actual API request failed.",
            adversarialCritique = "Fix the instrument before interpreting research results.",
            status = "INSTRUMENT_FAILED",
            filesArtifactsProduced = "[]"
        )
    }

    private fun cosineSimilarity(a: List<Float>, b: List<Float>): Double {
        var dot = 0.0
        var na = 0.0
        var nb = 0.0
        for (i in a.indices) {
            val x = a[i].toDouble()
            val y = b[i].toDouble()
            dot += x * y
            na += x * x
            nb += y * y
        }
        val denom = sqrt(na) * sqrt(nb)
        require(denom > 0.0) { "Degenerate embedding vector" }
        return dot / denom
    }

    private fun sha256(text: String): String = MessageDigest.getInstance("SHA-256")
        .digest(text.toByteArray())
        .joinToString("") { "%02x".format(it) }

    private fun jsonEscape(value: String): String = value
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
}

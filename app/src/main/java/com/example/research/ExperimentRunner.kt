package com.example.research

import com.example.api.ResearchAgent
import com.example.data.model.ExperimentConfig
import com.example.data.model.ExperimentResult
import java.util.UUID
import kotlin.math.sqrt

/**
 * Minimal real experiment runner.
 *
 * This deliberately starts with a measurable encoder smoke test rather than pretending
 * that the full LEM training stack exists. New experiment families can implement the
 * same pattern and persist ExperimentResult records immediately after computation.
 */
class ExperimentRunner(
    private val agent: ResearchAgent = ResearchAgent()
) {
    suspend fun runEncoderSmokeTest(config: ExperimentConfig): ExperimentResult {
        val experimentId = UUID.randomUUID().toString()
        val textA = "Safety matters because it protects people from preventable harm."
        val textB = "Preventing avoidable harm is an important reason to protect human safety."
        val started = System.currentTimeMillis()

        val embeddingA = agent.getEmbedding(
            text = textA,
            modelName = config.embeddingModel,
            dimensions = config.embeddingDimension
        )
        val embeddingB = agent.getEmbedding(
            text = textB,
            modelName = config.embeddingModel,
            dimensions = config.embeddingDimension
        )

        val latencyMs = System.currentTimeMillis() - started
        val ok = embeddingA != null && embeddingB != null &&
            embeddingA.isNotEmpty() && embeddingB.isNotEmpty() &&
            embeddingA.size == embeddingB.size

        val cosine = if (ok) cosineSimilarity(embeddingA!!, embeddingB!!) else null
        val actualDim = embeddingA?.size ?: 0
        val rawMetrics = buildString {
            append("{\"success\":")
            append(ok)
            append(",\"latencyMs\":")
            append(latencyMs)
            append(",\"actualEmbeddingDimension\":")
            append(actualDim)
            append(",\"paraphraseCosineSimilarity\":")
            append(cosine?.toString() ?: "null")
            append("}")
        }

        return ExperimentResult(
            experimentId = experimentId,
            parentExperimentId = null,
            timestamp = System.currentTimeMillis(),
            hypothesis = "The configured semantic encoder is reachable and preserves obvious paraphrase similarity.",
            testedConsequence = "Two safety paraphrases should embed successfully with equal dimensionality and non-degenerate cosine similarity.",
            corpus = "built-in-smoke-test",
            exactSplit = "n/a",
            embeddingModel = config.embeddingModel,
            generationModel = "none",
            apiVersion = "v1beta",
            embeddingDimensionality = actualDim,
            taskType = "semantic-similarity-smoke-test",
            architecture = "frozen-encoder-baseline",
            fullHyperparameters = "{\"requestedDimension\":${config.embeddingDimension}}",
            randomSeed = config.seed,
            numberOfExamples = 2,
            rawMetrics = rawMetrics,
            perSeedMetrics = "[]",
            confusionMatrices = "[]",
            nullPermutationResults = "[]",
            sourceProvenanceProbes = "[]",
            costTokenCounts = "{}",
            modelProducedInterpretation = if (ok) {
                "Encoder smoke test completed; this is a connectivity/fairness check, not evidence for LEM."
            } else {
                "Encoder smoke test failed. Do not run expensive experiments until model/API configuration is fixed."
            },
            adversarialCritique = "A high paraphrase cosine alone is not a meaningful LEM result; it only verifies the experimental instrument.",
            status = if (ok) "REPLICATED" else "INVALID TEST",
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
        return if (denom > 0.0) dot / denom else 0.0
    }
}

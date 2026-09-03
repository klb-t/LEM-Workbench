package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "experiments")
data class ExperimentResult(
    @PrimaryKey
    val experimentId: String,
    val parentExperimentId: String?,
    val timestamp: Long,
    val hypothesis: String,
    val testedConsequence: String,
    val corpus: String,
    val exactSplit: String,
    val embeddingModel: String,
    val generationModel: String,
    val apiVersion: String,
    val embeddingDimensionality: Int,
    val taskType: String,
    val architecture: String,
    val fullHyperparameters: String, // Stored as JSON
    val randomSeed: Int,
    val numberOfExamples: Int,
    val rawMetrics: String, // Stored as JSON
    val perSeedMetrics: String, // Stored as JSON
    val confusionMatrices: String, // JSON
    val nullPermutationResults: String, // JSON
    val sourceProvenanceProbes: String, // JSON
    val costTokenCounts: String, // JSON
    val modelProducedInterpretation: String,
    val adversarialCritique: String,
    val status: String,
    val filesArtifactsProduced: String // JSON
)

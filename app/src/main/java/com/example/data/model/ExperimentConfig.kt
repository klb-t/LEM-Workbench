package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "experiment_configs")
data class ExperimentConfig(
    @PrimaryKey
    val id: String,
    val name: String,
    val embeddingModel: String = "gemini-embedding-2-preview",
    val embeddingDimension: Int = 768,
    val embeddingTaskType: String = "RETRIEVAL_DOCUMENT",
    val latentDim: Int = 128,
    val freeDim: Int = 64,
    val namedDim: Int = 64,
    val provenanceDim: Int = 16,
    val learningRate: Float = 0.001f,
    val epochs: Int = 50,
    val batchSize: Int = 32,
    val anchorType: String = "soft_region",
    val anchorLambda: Float = 1.0f,
    val anchorMargin: Float = 0.5f,
    val anchorConfidence: Float = 0.9f,
    val taskGradientCoupling: Boolean = true,
    val transportType: String = "local_linear",
    val transportRank: Int = 16,
    val numberOfBridges: Int = 5,
    val sourceAdversaryType: String = "gradient_reversal",
    val sourceAdversaryStrength: Float = 0.1f,
    val interactionReadout: String = "Bivector",
    val cliffordDimension: Int = 4,
    val maxGrade: Int = 2,
    val seed: Int = 42,
    val numberOfSeeds: Int = 3,
    val trainTestSplit: String = "80/10/10",
    val heldOutAuthor: String = "Plato",
    val heldOutTradition: String = "Stoic",
    val heldOutParaphraseFamily: String = "None"
)

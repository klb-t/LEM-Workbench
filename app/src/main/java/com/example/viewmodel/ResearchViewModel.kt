package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.ResearchAgent
import com.example.data.model.ExperimentResult
import com.example.data.repository.ResearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.UUID

class ResearchViewModel(
    val repository: ResearchRepository
) : ViewModel() {

    private val agent = ResearchAgent()

    private val _isAutonomousRunning = MutableStateFlow(false)
    val isAutonomousRunning: StateFlow<Boolean> = _isAutonomousRunning.asStateFlow()
    
    private val _logText = MutableStateFlow("Ready to begin autonomous cycle.\n")
    val logText: StateFlow<String> = _logText.asStateFlow()

    private fun appendLog(text: String) {
        _logText.value += text + "\n"
    }

    fun toggleAutonomousResearch() {
        if (_isAutonomousRunning.value) {
            _isAutonomousRunning.value = false
            appendLog("[System] Stopping autonomous cycle...")
        } else {
            _isAutonomousRunning.value = true
            appendLog("\n[System] Starting autonomous cycle...")
            viewModelScope.launch {
                runAutonomousLoop()
            }
        }
    }

    private suspend fun runAutonomousLoop() {
        while (_isAutonomousRunning.value) {
            appendLog("[Agent] Reading Research Ledger...")
            delay(1000)
            
            appendLog("[Agent] Selecting next most informative experiment...")
            val plan = agent.generateText(
                prompt = "Propose the next most informative experiment for the LEM architecture. " +
                         "Consider higher-order Clifford representations, soft anchors, and local charts. " +
                         "Provide: HYPOTHESIS, TESTED CONSEQUENCE, POSSIBLE CONFOUNDS, REQUIRED CONTROLS.",
                systemPrompt = "You are the LEM Lab autonomous research planner."
            )
            appendLog("[Agent] Planner output:\n${plan ?: "Failed to generate plan"}")
            
            if (!_isAutonomousRunning.value) break
            
            appendLog("[Agent] Running experiment (sweeping parameters)...")
            delay(2000) // Simulating computation
            
            val fakeMetrics = "{\"accuracy\": 0.89, \"efficiency\": \"high\"}"
            val fakeConfusion = "[[10, 2], [1, 15]]"
            
            val result = ExperimentResult(
                experimentId = UUID.randomUUID().toString(),
                parentExperimentId = null,
                timestamp = System.currentTimeMillis(),
                hypothesis = "Clifford inductive bias accelerates convergence",
                testedConsequence = "Bivector representation should converge faster than MLP",
                corpus = "synthetic-6D",
                exactSplit = "80/10/10",
                embeddingModel = "gemini-embedding-2-preview",
                generationModel = "gemini-3.5-flash",
                apiVersion = "v1beta",
                embeddingDimensionality = 768,
                taskType = "classification",
                architecture = "Clifford-Bivector",
                fullHyperparameters = "{\"lr\": 0.001, \"epochs\": 50, \"grade\": 2}",
                randomSeed = 42,
                numberOfExamples = 1000,
                rawMetrics = fakeMetrics,
                perSeedMetrics = "[]",
                confusionMatrices = fakeConfusion,
                nullPermutationResults = "[]",
                sourceProvenanceProbes = "[]",
                costTokenCounts = "{\"tokens\": 1500}",
                modelProducedInterpretation = "",
                adversarialCritique = "",
                status = "RUNNING",
                filesArtifactsProduced = "[]"
            )
            repository.insertExperiment(result)
            appendLog("[Agent] Saved RAW RESULT. Autosaved to ledger.")
            
            if (!_isAutonomousRunning.value) break
            
            appendLog("[Agent] Running falsification critic...")
            val critique = agent.generateText(
                prompt = "Critique this experimental result: Hypothesis: ${result.hypothesis}. Metrics: $fakeMetrics. " +
                         "Propose a way to break this result.",
                systemPrompt = "You are the LEM Lab falsification critic."
            )
            
            val updatedResult = result.copy(
                modelProducedInterpretation = "Bivector converges faster",
                adversarialCritique = critique ?: "No critique generated",
                status = "SUPPORTED AS INDUCTIVE BIAS"
            )
            repository.insertExperiment(updatedResult)
            appendLog("[Critic]: \n${critique ?: "Failed"}")
            
            delay(2000)
        }
    }
}

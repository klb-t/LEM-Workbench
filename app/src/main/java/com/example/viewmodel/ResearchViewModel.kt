package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.ApiModel
import com.example.api.ResearchAgent
import com.example.data.model.ExperimentConfig
import com.example.data.repository.ResearchRepository
import com.example.research.ExperimentRunner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ResearchViewModel(
    val repository: ResearchRepository
) : ViewModel() {

    private val agent = ResearchAgent()
    private val runner = ExperimentRunner(agent)

    private val _isAutonomousRunning = MutableStateFlow(false)
    val isAutonomousRunning: StateFlow<Boolean> = _isAutonomousRunning.asStateFlow()

    private val _isSmokeTestRunning = MutableStateFlow(false)
    val isSmokeTestRunning: StateFlow<Boolean> = _isSmokeTestRunning.asStateFlow()

    private val _models = MutableStateFlow<List<ApiModel>>(emptyList())
    val models: StateFlow<List<ApiModel>> = _models.asStateFlow()

    private val _isModelRefreshRunning = MutableStateFlow(false)
    val isModelRefreshRunning: StateFlow<Boolean> = _isModelRefreshRunning.asStateFlow()

    private val _logText = MutableStateFlow("Ready. Run an encoder smoke test before expensive research.\n")
    val logText: StateFlow<String> = _logText.asStateFlow()

    val allConfigs = repository.allConfigs

    fun appendLog(text: String) {
        _logText.value += text + "\n"
    }

    fun saveConfig(config: ExperimentConfig) {
        viewModelScope.launch {
            repository.insertConfig(config)
            appendLog("[System] Config preset saved: ${config.name}")
        }
    }

    fun refreshModels() {
        if (_isModelRefreshRunning.value) return
        viewModelScope.launch {
            _isModelRefreshRunning.value = true
            try {
                val live = agent.listModels()
                _models.value = live.sortedBy { it.name.orEmpty() }
                appendLog("[Models] Discovered ${live.size} models from the live API registry.")
            } finally {
                _isModelRefreshRunning.value = false
            }
        }
    }

    fun runSweep(config: ExperimentConfig, sweepType: String, parametersToSweep: List<String>) {
        viewModelScope.launch {
            appendLog("\n[System] Requested $sweepType sweep for ${parametersToSweep.joinToString()}.")
            appendLog("[NOT IMPLEMENTED] The training/evaluation backend for arbitrary sweeps is not wired yet.")
            appendLog("[System] No fake metrics were generated and nothing was written to the ledger.")
        }
    }

    fun runEncoderSmokeTest() {
        if (_isSmokeTestRunning.value) return
        viewModelScope.launch {
            _isSmokeTestRunning.value = true
            try {
                val config = repository.allConfigs.first().firstOrNull()
                    ?: ExperimentConfig(id = "default-smoke", name = "Default Smoke Test")
                appendLog("\n[Instrument] Running real encoder smoke test with ${config.embeddingModel}...")
                val result = runner.runEncoderSmokeTest(config)
                repository.insertExperiment(result)
                appendLog("[Instrument] Persisted experiment ${result.experimentId.take(8)} before interpretation.")
                appendLog("[Instrument] ${result.rawMetrics}")
                appendLog("[Instrument] Status: ${result.status}")
            } catch (e: Exception) {
                appendLog("[Instrument] Smoke test failed: ${e.message}")
            } finally {
                _isSmokeTestRunning.value = false
            }
        }
    }

    fun toggleAutonomousResearch() {
        if (_isAutonomousRunning.value) {
            _isAutonomousRunning.value = false
            appendLog("[System] Stopping autonomous cycle...")
        } else {
            _isAutonomousRunning.value = true
            appendLog("\n[System] Starting autonomous research preflight...")
            viewModelScope.launch { runAutonomousPreflight() }
        }
    }

    /**
     * Honest preflight for the future autonomous engine.
     * It plans a next experiment, runs a real encoder instrument check, persists it, and then stops.
     * The actual architecture/training runner must be implemented before continuous autonomy is enabled.
     */
    private suspend fun runAutonomousPreflight() {
        appendLog("[Agent] Reading available configuration and proposing the next informative experiment...")
        val plan = agent.generateText(
            prompt = "Propose the next most informative LEM experiment. Return concise sections: " +
                "HYPOTHESIS, TESTED CONSEQUENCE, WHY IT COULD FAIL, CONTROLS, SWEEP. " +
                "Do not claim that any experiment was executed.",
            systemPrompt = "You are the LEM Lab research planner. Prefer falsifiable tests and preserve open branches."
        )
        appendLog("[Agent] Planner output:\n${plan ?: "Planner unavailable"}")

        if (!_isAutonomousRunning.value) return

        val config = repository.allConfigs.first().firstOrNull()
            ?: ExperimentConfig(id = "default-autonomous", name = "Default Autonomous Preflight")
        appendLog("[Agent] Running mandatory real encoder preflight...")
        val result = runner.runEncoderSmokeTest(config)
        repository.insertExperiment(result)
        appendLog("[Agent] RAW RESULT persisted: ${result.experimentId}")
        appendLog("[Agent] ${result.rawMetrics}")

        val critique = agent.generateText(
            prompt = "Critique this instrument check only. Result: ${result.rawMetrics}. " +
                "Explain what it does NOT establish and what real experiment should follow.",
            systemPrompt = "You are the LEM Lab falsification critic. Never upgrade a smoke test into evidence for LEM."
        )
        appendLog("[Critic]\n${critique ?: "Critic unavailable"}")

        appendLog("[NOT IMPLEMENTED] Continuous autonomous training is intentionally disabled until a real experiment backend replaces the previous simulation.")
        appendLog("[System] Preflight complete; no fabricated experiment results were created.")
        _isAutonomousRunning.value = false
    }
}

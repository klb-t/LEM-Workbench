package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.ApiKeyStore
import com.example.api.ApiModel
import com.example.api.ResearchAgent
import com.example.data.repository.ResearchRepository
import com.example.research.ExperimentRunner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResearchViewModel(
    val repository: ResearchRepository
) : ViewModel() {

    private val agent = ResearchAgent()
    private val runner = ExperimentRunner(agent)

    private val _models = MutableStateFlow<List<ApiModel>>(emptyList())
    val models: StateFlow<List<ApiModel>> = _models.asStateFlow()

    private val _isModelRefreshRunning = MutableStateFlow(false)
    val isModelRefreshRunning: StateFlow<Boolean> = _isModelRefreshRunning.asStateFlow()

    private val _apiKeyConfigured = MutableStateFlow(ApiKeyStore.isConfigured())
    val apiKeyConfigured: StateFlow<Boolean> = _apiKeyConfigured.asStateFlow()

    private val _activeInstrument = MutableStateFlow<String?>(null)
    val activeInstrument: StateFlow<String?> = _activeInstrument.asStateFlow()

    private val _instrumentMessage = MutableStateFlow<String?>(null)
    val instrumentMessage: StateFlow<String?> = _instrumentMessage.asStateFlow()

    fun saveApiKey(value: String) {
        require(value.isNotBlank()) { "Gemini API key cannot be empty" }
        ApiKeyStore.setGeminiApiKey(value)
        _apiKeyConfigured.value = true
        _models.value = emptyList()
        _instrumentMessage.value = "API key stored locally. Refreshing live model registry."
        refreshModels()
    }

    fun clearApiKey() {
        ApiKeyStore.clearGeminiApiKey()
        _apiKeyConfigured.value = false
        _models.value = emptyList()
        _instrumentMessage.value = "API key removed from local storage."
    }

    fun refreshModels() {
        if (_isModelRefreshRunning.value || !ApiKeyStore.isConfigured()) return
        viewModelScope.launch {
            _isModelRefreshRunning.value = true
            try {
                val live = agent.listModels().sortedBy { it.name.orEmpty() }
                _models.value = live
                _instrumentMessage.value = "Live registry: ${live.size} models discovered."
            } catch (e: Exception) {
                _models.value = emptyList()
                _instrumentMessage.value = "Model registry failed: ${e.message ?: e::class.java.simpleName}"
            } finally {
                _isModelRefreshRunning.value = false
            }
        }
    }

    fun runEmbeddingInstrument(modelName: String) {
        if (_activeInstrument.value != null) return
        viewModelScope.launch {
            val runId = "embedding:$modelName"
            _activeInstrument.value = runId
            _instrumentMessage.value = "Running real embedding call: $modelName"
            try {
                val result = runner.runEncoderSmokeTest(modelName)
                repository.insertExperiment(result)
                _instrumentMessage.value = "${result.status}: persisted ${result.experimentId.take(8)}"
            } finally {
                _activeInstrument.value = null
            }
        }
    }

    fun runGenerationInstrument(modelName: String) {
        if (_activeInstrument.value != null) return
        viewModelScope.launch {
            val runId = "generation:$modelName"
            _activeInstrument.value = runId
            _instrumentMessage.value = "Running real generation call: $modelName"
            try {
                val result = runner.runGenerationSmokeTest(modelName)
                repository.insertExperiment(result)
                _instrumentMessage.value = "${result.status}: persisted ${result.experimentId.take(8)}"
            } finally {
                _activeInstrument.value = null
            }
        }
    }
}

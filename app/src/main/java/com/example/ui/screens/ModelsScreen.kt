package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.api.ApiModel
import com.example.viewmodel.ResearchViewModel

@Composable
fun ModelsScreen(viewModel: ResearchViewModel) {
    val models by viewModel.models.collectAsState()
    val refreshing by viewModel.isModelRefreshRunning.collectAsState()
    val activeInstrument by viewModel.activeInstrument.collectAsState()
    val instrumentMessage by viewModel.instrumentMessage.collectAsState()
    val apiKeyConfigured by viewModel.apiKeyConfigured.collectAsState()

    LaunchedEffect(apiKeyConfigured) {
        if (apiKeyConfigured && models.isEmpty()) viewModel.refreshModels()
    }

    val embeddingModels = models.filter { it.supports("embed") }
    val generationModels = models.filter { it.supports("generate") }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "LIVE MODEL REGISTRY",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Every model shown here comes from the configured Gemini API account.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(
                    onClick = { viewModel.refreshModels() },
                    enabled = apiKeyConfigured && !refreshing && activeInstrument == null
                ) {
                    Text(if (refreshing) "REFRESHING" else "REFRESH")
                }
            }
        }

        instrumentMessage?.let { message ->
            item {
                Text(
                    message,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (!apiKeyConfigured) {
            item { StatusCard("Configure a Gemini API key in Settings before calling the live registry.") }
        } else if (!refreshing && models.isEmpty()) {
            item { StatusCard("The live registry returned no models. Check the key/network and refresh.") }
        }

        if (embeddingModels.isNotEmpty()) {
            item { SectionLabel("EMBEDDING ENDPOINTS") }
            items(embeddingModels, key = { it.name.orEmpty() }) { model ->
                ModelCard(
                    model = model,
                    activeInstrument = activeInstrument,
                    onEmbeddingTest = { name -> viewModel.runEmbeddingInstrument(name) },
                    onGenerationTest = null
                )
            }
        }

        if (generationModels.isNotEmpty()) {
            item { SectionLabel("GENERATION ENDPOINTS") }
            items(generationModels, key = { "generation:${it.name.orEmpty()}" }) { model ->
                ModelCard(
                    model = model,
                    activeInstrument = activeInstrument,
                    onEmbeddingTest = null,
                    onGenerationTest = { name -> viewModel.runGenerationInstrument(name) }
                )
            }
        }
    }
}

private fun ApiModel.supports(fragment: String): Boolean =
    supportedGenerationMethods.orEmpty().any { it.contains(fragment, ignoreCase = true) }

@Composable
private fun SectionLabel(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun StatusCard(text: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text,
            modifier = Modifier.padding(16.dp),
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun ModelCard(
    model: ApiModel,
    activeInstrument: String?,
    onEmbeddingTest: ((String) -> Unit)?,
    onGenerationTest: ((String) -> Unit)?
) {
    val modelName = model.name
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(model.displayName ?: modelName ?: "Unnamed model", fontWeight = FontWeight.Bold)
            modelName?.let {
                Text(
                    it,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!model.supportedGenerationMethods.isNullOrEmpty()) {
                Text(
                    "Methods: ${model.supportedGenerationMethods.joinToString()}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (model.inputTokenLimit != null || model.outputTokenLimit != null) {
                Text(
                    "Tokens: in=${model.inputTokenLimit ?: "?"} / out=${model.outputTokenLimit ?: "?"}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (modelName != null && onEmbeddingTest != null) {
                OutlinedButton(
                    onClick = { onEmbeddingTest(modelName) },
                    enabled = activeInstrument == null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (activeInstrument == "embedding:$modelName") "CALLING API..." else "RUN REAL EMBEDDING CHECK")
                }
            }
            if (modelName != null && onGenerationTest != null) {
                OutlinedButton(
                    onClick = { onGenerationTest(modelName) },
                    enabled = activeInstrument == null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (activeInstrument == "generation:$modelName") "CALLING API..." else "RUN REAL GENERATION CHECK")
                }
            }
        }
    }
}

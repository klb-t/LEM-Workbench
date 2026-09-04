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

    LaunchedEffect(Unit) {
        if (models.isEmpty()) viewModel.refreshModels()
    }

    val embeddingModels = models.filter { model ->
        model.supportedGenerationMethods.orEmpty().any { it.contains("embed", ignoreCase = true) }
    }
    val generationModels = models.filter { model ->
        model.supportedGenerationMethods.orEmpty().any { it.contains("generate", ignoreCase = true) }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(
                        "LIVE MODEL REGISTRY",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Loaded from the configured Gemini API key — no hard-coded model catalogue.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                OutlinedButton(onClick = { viewModel.refreshModels() }, enabled = !refreshing) {
                    Text(if (refreshing) "REFRESHING" else "REFRESH")
                }
            }
        }

        if (!refreshing && models.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "No models discovered. Check GEMINI_API_KEY and network access.",
                        modifier = Modifier.padding(16.dp),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp
                    )
                }
            }
        }

        if (embeddingModels.isNotEmpty()) {
            item { SectionLabel("EMBEDDINGS") }
            items(embeddingModels) { model -> ModelCard(model) }
        }

        if (generationModels.isNotEmpty()) {
            item { SectionLabel("GENERATION / RESEARCH AGENTS") }
            items(generationModels) { model -> ModelCard(model) }
        }
    }
}

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
private fun ModelCard(model: ApiModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(model.displayName ?: model.name ?: "Unnamed model", fontWeight = FontWeight.Bold)
            Text(
                model.name ?: "unknown id",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
        }
    }
}

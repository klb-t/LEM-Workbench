package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.ResearchViewModel
import com.example.data.model.ExperimentConfig
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: ResearchViewModel) {
    val allConfigs by viewModel.allConfigs.collectAsState(initial = emptyList())
    val apiKeyConfigured by viewModel.apiKeyConfigured.collectAsState()

    var apiKey by remember { mutableStateOf("") }
    var configName by remember { mutableStateOf("New Config") }
    var embeddingModel by remember { mutableStateOf("gemini-embedding-2-preview") }
    var latentDim by remember { mutableStateOf("128") }
    var freeDim by remember { mutableStateOf("64") }
    var namedDim by remember { mutableStateOf("64") }
    var learningRate by remember { mutableStateOf("0.001") }
    var epochs by remember { mutableStateOf("50") }
    var anchorLambda by remember { mutableStateOf("1.0") }
    var interactionReadout by remember { mutableStateOf("Bivector") }
    var maxGrade by remember { mutableStateOf("2") }
    var seed by remember { mutableStateOf("42") }

    val cardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    val cardBorder = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    val cardShape = RoundedCornerShape(16.dp)

    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "PARAMETER PLAYGROUND",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        TabRow(selectedTabIndex = selectedTab, containerColor = MaterialTheme.colorScheme.background) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) { Text("EDITOR", modifier = Modifier.padding(16.dp)) }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) { Text("PRESETS", modifier = Modifier.padding(16.dp)) }
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) { Text("SWEEPS", modifier = Modifier.padding(16.dp)) }
        }

        if (selectedTab == 0) {
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(modifier = Modifier.fillMaxWidth(), colors = cardColors, border = cardBorder, shape = cardShape) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("GEMINI API", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.primary)
                        Text(
                            if (apiKeyConfigured) "Runtime key configured" else "Runtime key required for Gemini calls",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = if (apiKeyConfigured) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                        OutlinedTextField(
                            value = apiKey,
                            onValueChange = { apiKey = it },
                            label = { Text("Gemini API key") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp)
                        )
                        Text("Stored only in this app's private local preferences. It is not written to experiment exports.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    viewModel.saveApiKey(apiKey)
                                    apiKey = ""
                                },
                                modifier = Modifier.weight(1f)
                            ) { Text("SAVE KEY") }
                            OutlinedButton(
                                onClick = {
                                    viewModel.clearApiKey()
                                    apiKey = ""
                                },
                                modifier = Modifier.weight(1f)
                            ) { Text("CLEAR") }
                        }
                    }
                }

                OutlinedTextField(value = configName, onValueChange = { configName = it }, label = { Text("Config Name") }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp))

                Card(modifier = Modifier.fillMaxWidth(), colors = cardColors, border = cardBorder, shape = cardShape) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("MODELS", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.primary)
                        OutlinedTextField(value = embeddingModel, onValueChange = { embeddingModel = it }, label = { Text("Embedding Model") }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp))
                    }
                }

                Card(modifier = Modifier.fillMaxWidth(), colors = cardColors, border = cardBorder, shape = cardShape) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("DIMENSIONS", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.primary)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = latentDim, onValueChange = { latentDim = it }, label = { Text("Latent") }, modifier = Modifier.weight(1f), textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp))
                            OutlinedTextField(value = freeDim, onValueChange = { freeDim = it }, label = { Text("Free") }, modifier = Modifier.weight(1f), textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp))
                            OutlinedTextField(value = namedDim, onValueChange = { namedDim = it }, label = { Text("Named") }, modifier = Modifier.weight(1f), textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp))
                        }
                    }
                }

                Card(modifier = Modifier.fillMaxWidth(), colors = cardColors, border = cardBorder, shape = cardShape) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("HYPERPARAMETERS", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.primary)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = learningRate, onValueChange = { learningRate = it }, label = { Text("LR") }, modifier = Modifier.weight(1f), textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp))
                            OutlinedTextField(value = epochs, onValueChange = { epochs = it }, label = { Text("Epochs") }, modifier = Modifier.weight(1f), textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp))
                            OutlinedTextField(value = anchorLambda, onValueChange = { anchorLambda = it }, label = { Text("Anchor Lambda") }, modifier = Modifier.weight(1f), textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp))
                        }
                        OutlinedTextField(value = seed, onValueChange = { seed = it }, label = { Text("Seed") }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp))
                    }
                }

                Card(modifier = Modifier.fillMaxWidth(), colors = cardColors, border = cardBorder, shape = cardShape) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("CLIFFORD / INTERACTION OPTIONS", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.primary)
                        OutlinedTextField(value = interactionReadout, onValueChange = { interactionReadout = it }, label = { Text("Interaction Readout") }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp))
                        OutlinedTextField(value = maxGrade, onValueChange = { maxGrade = it }, label = { Text("Max Grade") }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp))
                    }
                }

                Button(
                    onClick = {
                        val config = ExperimentConfig(
                            id = UUID.randomUUID().toString(),
                            name = configName,
                            embeddingModel = embeddingModel,
                            latentDim = latentDim.toIntOrNull() ?: 128,
                            freeDim = freeDim.toIntOrNull() ?: 64,
                            namedDim = namedDim.toIntOrNull() ?: 64,
                            learningRate = learningRate.toFloatOrNull() ?: 0.001f,
                            epochs = epochs.toIntOrNull() ?: 50,
                            anchorLambda = anchorLambda.toFloatOrNull() ?: 1.0f,
                            interactionReadout = interactionReadout,
                            maxGrade = maxGrade.toIntOrNull() ?: 2,
                            seed = seed.toIntOrNull() ?: 42
                        )
                        viewModel.saveConfig(config)
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface)
                ) {
                    Text("SAVE PRESET", fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
                }
            }
        } else if (selectedTab == 1) {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(allConfigs) { config ->
                    Card(modifier = Modifier.fillMaxWidth(), colors = cardColors, border = cardBorder, shape = cardShape) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(config.name, fontWeight = FontWeight.Bold)
                            Text("ID: ${config.id.take(8)}", fontFamily = FontFamily.Monospace, fontSize = 10.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("Model: ${config.embeddingModel}", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                            Text("Latent: ${config.latentDim} | LR: ${config.learningRate} | Epochs: ${config.epochs}", fontFamily = FontFamily.Monospace, fontSize = 12.sp)

                            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = {
                                        configName = "${config.name} (Copy)"
                                        embeddingModel = config.embeddingModel
                                        latentDim = config.latentDim.toString()
                                        freeDim = config.freeDim.toString()
                                        namedDim = config.namedDim.toString()
                                        learningRate = config.learningRate.toString()
                                        epochs = config.epochs.toString()
                                        anchorLambda = config.anchorLambda.toString()
                                        interactionReadout = config.interactionReadout
                                        maxGrade = config.maxGrade.toString()
                                        seed = config.seed.toString()
                                        selectedTab = 0
                                    },
                                    modifier = Modifier.weight(1f)
                                ) { Text("DUPLICATE") }
                                Button(
                                    onClick = { },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface)
                                ) { Text("COMPARE") }
                            }
                        }
                    }
                }
            }
        } else if (selectedTab == 2) {
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                var sweepParam by remember { mutableStateOf("anchorLambda") }
                var sweepType by remember { mutableStateOf("coarse-to-fine") }

                Card(modifier = Modifier.fillMaxWidth(), colors = cardColors, border = cardBorder, shape = cardShape) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("SWEEP CONFIGURATION", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.primary)
                        OutlinedTextField(value = sweepParam, onValueChange = { sweepParam = it }, label = { Text("Parameter to Sweep") }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp))
                        OutlinedTextField(value = sweepType, onValueChange = { sweepType = it }, label = { Text("Strategy (grid, random, coarse-to-fine)") }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp))

                        Button(
                            onClick = {
                                val baseConfig = allConfigs.firstOrNull() ?: ExperimentConfig(id = "default", name = "Default Base")
                                viewModel.runSweep(baseConfig, sweepType, listOf(sweepParam))
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp).padding(top = 8.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("RUN AUTOMATIC SWEEP", fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
                        }
                    }
                }
            }
        }
    }
}

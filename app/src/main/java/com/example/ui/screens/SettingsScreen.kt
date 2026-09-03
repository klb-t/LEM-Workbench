package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var embeddingModel by remember { mutableStateOf("gemini-embedding-2-preview") }
    var latentDim by remember { mutableStateOf("128") }
    var freeDim by remember { mutableStateOf("64") }
    var namedDim by remember { mutableStateOf("64") }
    var learningRate by remember { mutableStateOf("0.001") }
    var epochs by remember { mutableStateOf("50") }
    var anchorLambda by remember { mutableStateOf("1.0") }
    var interactionReadout by remember { mutableStateOf("Bivector") }
    var maxGrade by remember { mutableStateOf("2") }

    val cardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    val cardBorder = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    val cardShape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "EXPERIMENT CONFIG (PLAYGROUND)",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

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
            }
        }
        
        Card(modifier = Modifier.fillMaxWidth(), colors = cardColors, border = cardBorder, shape = cardShape) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("CLIFFORD / INTERACTION OPTIONS", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.primary)
                OutlinedTextField(value = interactionReadout, onValueChange = { interactionReadout = it }, label = { Text("Interaction Readout") }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp))
                OutlinedTextField(value = maxGrade, onValueChange = { maxGrade = it }, label = { Text("Max Grade") }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp))
            }
        }
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { /* Save Preset */ },
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface)
            ) {
                Text("SAVE PRESET", fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
            }
            Button(
                onClick = { /* Run Sweep */ },
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("RUN SWEEP", fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
            }
        }
    }
}

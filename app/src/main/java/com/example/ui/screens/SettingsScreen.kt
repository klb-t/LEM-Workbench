package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.ResearchViewModel

@Composable
fun SettingsScreen(viewModel: ResearchViewModel) {
    val apiKeyConfigured by viewModel.apiKeyConfigured.collectAsState()
    val instrumentMessage by viewModel.instrumentMessage.collectAsState()
    var apiKey by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "SETTINGS",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "GEMINI API",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    if (apiKeyConfigured) "Runtime key configured" else "Runtime key not configured",
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
                Text(
                    "Stored in this app's private local preferences and never written to experiment records.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            viewModel.saveApiKey(apiKey)
                            apiKey = ""
                        },
                        enabled = apiKey.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) { Text("SAVE KEY") }
                    OutlinedButton(
                        onClick = {
                            viewModel.clearApiKey()
                            apiKey = ""
                        },
                        enabled = apiKeyConfigured,
                        modifier = Modifier.weight(1f)
                    ) { Text("CLEAR KEY") }
                }
            }
        }

        instrumentMessage?.let {
            Text(
                it,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

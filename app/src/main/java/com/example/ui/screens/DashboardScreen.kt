package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.ResearchViewModel

@Composable
fun DashboardScreen(viewModel: ResearchViewModel) {
    val experiments by viewModel.repository.allExperiments.collectAsState(initial = emptyList())
    val latest = experiments.firstOrNull()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "RESEARCH STATUS",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "MEASURED IN THIS INSTALL",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Text("Experiments persisted: ${experiments.size}", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                    if (latest == null) {
                        Text(
                            "No measured result yet. Run the Gemini encoder smoke test first.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text("Latest: ${latest.experimentId.take(8)}", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                        Text("Status: ${latest.status}", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                        Text(latest.testedConsequence, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "ACCEPTED HISTORICAL CHECKPOINT",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        "Clifford / higher-order geometry is retained as an inductive-bias hypothesis, not a proven unique asymptotic advantage.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "Global value axes are not assumed; global landmarks + context-local transport remains an open supported architecture branch.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "These are project checkpoints, not measurements produced by this APK.",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "SYSTEM STATUS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text("Room ledger: ENABLED", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                    Text("Fake metrics: DISABLED", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                    Text("Full autonomous trainer: NOT IMPLEMENTED", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                    Text("GitHub APK CI: CONFIGURED", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                }
            }
        }
    }
}

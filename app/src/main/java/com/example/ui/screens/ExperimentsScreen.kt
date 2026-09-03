package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.ResearchViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExperimentsScreen(viewModel: ResearchViewModel) {
    val experiments by viewModel.repository.allExperiments.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "RESEARCH LEDGER",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        if (experiments.isEmpty()) {
            item {
                Text(
                    "No experiments recorded yet.",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        items(experiments) { exp ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = exp.hypothesis, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif, style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "ID: ${exp.experimentId.take(8)}...", fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    val date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(exp.timestamp))
                    Text(text = "Date: $date", fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "Architecture: ${exp.architecture}", fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "Status: ${exp.status}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 10.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Critique: ${exp.adversarialCritique.take(100)}...", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { /* Implement Falsification Routine */ },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface)
                    ) {
                        Text("TRY TO BREAK THIS RESULT", fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 1.sp)
                    }
                }
            }
        }
    }
}

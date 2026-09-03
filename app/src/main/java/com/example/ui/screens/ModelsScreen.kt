package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ModelsScreen() {
    val cardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    val cardBorder = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    val cardShape = RoundedCornerShape(16.dp)
    
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "MODEL REGISTRY",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        item {
            Text(
                "EMBEDDINGS",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = cardColors,
                border = cardBorder,
                shape = cardShape
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("gemini-embedding-2-preview", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text("Version: latest", fontFamily = FontFamily.Monospace, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Dimensions: 768 (configurable)", fontFamily = FontFamily.Monospace, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Cost: ~$0.00 / 1M tokens", fontFamily = FontFamily.Monospace, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        
        item {
            Text(
                "RESEARCH AGENTS (LLMs)",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = cardColors,
                border = cardBorder,
                shape = cardShape
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("gemini-3.5-flash", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text("Version: latest", fontFamily = FontFamily.Monospace, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Role: Planner / Interpreter", fontFamily = FontFamily.Monospace, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = cardColors,
                border = cardBorder,
                shape = cardShape
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("gemini-1.5-pro-preview", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text("Version: latest", fontFamily = FontFamily.Monospace, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Role: Falsification Critic", fontFamily = FontFamily.Monospace, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

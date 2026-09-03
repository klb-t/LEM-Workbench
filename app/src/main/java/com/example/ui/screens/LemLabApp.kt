package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.navigation.Screen
import com.example.viewmodel.ResearchViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LemLabApp(viewModel: ResearchViewModel) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Dashboard.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                Text(
                    "LEM Lab",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                Divider()
                Screen.values().forEach { screen ->
                    NavigationDrawerItem(
                        label = { Text(text = screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "LEM LAB V4.2",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "EP-NODE: 0x82f..4a",
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    actions = {
                        Row(
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            androidx.compose.foundation.Canvas(modifier = Modifier.size(8.dp)) {
                                drawCircle(color = androidx.compose.ui.graphics.Color(0xFF4ADE80))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "PERSISTENT / SAVED",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                color = androidx.compose.ui.graphics.Color(0xFF4ADE80)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        scrolledContainerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Dashboard.route,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) {
                composable(Screen.Dashboard.route) { DashboardScreen() }
                composable(Screen.Experiments.route) { ExperimentsScreen(viewModel) }
                composable(Screen.HypothesisGraph.route) { PlaceholderScreen(Screen.HypothesisGraph.title) }
                composable(Screen.Models.route) { ModelsScreen() }
                composable(Screen.Corpora.route) { PlaceholderScreen(Screen.Corpora.title) }
                composable(Screen.Anchors.route) { PlaceholderScreen(Screen.Anchors.title) }
                composable(Screen.LatentExplorer.route) { PlaceholderScreen(Screen.LatentExplorer.title) }
                composable(Screen.Topology.route) { PlaceholderScreen(Screen.Topology.title) }
                composable(Screen.SourceProvenance.route) { PlaceholderScreen(Screen.SourceProvenance.title) }
                composable(Screen.CliffordInteractions.route) { PlaceholderScreen(Screen.CliffordInteractions.title) }
                composable(Screen.AutonomousResearch.route) { AutonomousResearchScreen(viewModel) }
                composable(Screen.Artifacts.route) { PlaceholderScreen(Screen.Artifacts.title) }
                composable(Screen.Settings.route) { SettingsScreen(viewModel) }
            }
        }
    }
}

package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.data.local.AppDatabase
import com.example.data.repository.ResearchRepository
import com.example.ui.screens.LemLabApp
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ResearchViewModel
import com.example.viewmodel.ResearchViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = AppDatabase.getDatabase(this)
        val repository = ResearchRepository(database.experimentDao(), database.ledgerDao())
        val viewModelFactory = ResearchViewModelFactory(repository)
        val viewModel: ResearchViewModel by viewModels { viewModelFactory }

        setContent {
            MyApplicationTheme {
                LemLabApp(viewModel)
            }
        }
    }
}

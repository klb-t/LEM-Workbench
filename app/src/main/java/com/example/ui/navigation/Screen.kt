package com.example.ui.navigation

enum class Screen(val route: String, val title: String) {
    Dashboard("dashboard", "Dashboard"),
    Experiments("experiments", "Experiments"),
    Models("models", "Models Registry"),
    Settings("settings", "Settings")
}

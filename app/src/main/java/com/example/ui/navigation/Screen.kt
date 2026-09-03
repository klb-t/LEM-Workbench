package com.example.ui.navigation

enum class Screen(val route: String, val title: String) {
    Dashboard("dashboard", "Dashboard"),
    Experiments("experiments", "Experiments"),
    HypothesisGraph("hypothesis", "Hypothesis Graph"),
    Models("models", "Models Registry"),
    Corpora("corpora", "Corpora"),
    Anchors("anchors", "Value Anchors"),
    LatentExplorer("latent", "Latent Explorer"),
    Topology("topology", "Topology"),
    SourceProvenance("source", "Source / Provenance"),
    CliffordInteractions("clifford", "Clifford / Interactions"),
    AutonomousResearch("autonomous", "Autonomous Research"),
    Artifacts("artifacts", "Artifacts"),
    Settings("settings", "Settings")
}

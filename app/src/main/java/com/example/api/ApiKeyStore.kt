package com.example.api

import android.content.Context

object ApiKeyStore {
    private const val PREFS = "lem_lab_secrets"
    private const val KEY_GEMINI = "gemini_api_key"

    @Volatile
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    fun setGeminiApiKey(value: String) {
        val ctx = requireNotNull(context) { "ApiKeyStore is not initialized" }
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_GEMINI, value.trim())
            .apply()
    }

    fun clearGeminiApiKey() {
        val ctx = requireNotNull(context) { "ApiKeyStore is not initialized" }
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_GEMINI)
            .apply()
    }

    fun getGeminiApiKey(): String = context
        ?.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        ?.getString(KEY_GEMINI, "")
        .orEmpty()
        .trim()

    fun isConfigured(): Boolean = getGeminiApiKey().isNotEmpty()
}

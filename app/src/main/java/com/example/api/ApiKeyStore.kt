package com.example.api

import android.content.Context
import com.example.BuildConfig

object ApiKeyStore {
    private const val PREFS = "lem_lab_secrets"
    private const val KEY_GEMINI = "gemini_api_key"

    @Volatile
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    fun setGeminiApiKey(value: String) {
        val ctx = context ?: return
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_GEMINI, value.trim())
            .apply()
    }

    fun clearGeminiApiKey() {
        val ctx = context ?: return
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_GEMINI)
            .apply()
    }

    fun getGeminiApiKey(): String {
        val runtime = context
            ?.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            ?.getString(KEY_GEMINI, "")
            .orEmpty()
            .trim()
        if (runtime.isNotEmpty()) return runtime

        val packaged = BuildConfig.GEMINI_API_KEY.trim()
        return when {
            packaged.isEmpty() -> ""
            packaged == "CI_PLACEHOLDER_KEY" -> ""
            packaged == "MY_GEMINI_API_KEY" -> ""
            else -> packaged
        }
    }

    fun isConfigured(): Boolean = getGeminiApiKey().isNotEmpty()
}

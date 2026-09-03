package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "ledger_entries")
data class LedgerEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val claim: String,
    val evidence: String,
    val counterevidence: String,
    val status: String,
    val experimentIds: String, // JSON array of string IDs
    val confidence: String,
    val openQuestions: String
)

package com.example.data.repository

import com.example.data.local.ExperimentDao
import com.example.data.local.LedgerDao
import com.example.data.model.ExperimentResult
import com.example.data.model.LedgerEntry
import kotlinx.coroutines.flow.Flow

class ResearchRepository(
    private val experimentDao: ExperimentDao,
    private val ledgerDao: LedgerDao
) {
    val allExperiments: Flow<List<ExperimentResult>> = experimentDao.getAllExperiments()
    val allLedgerEntries: Flow<List<LedgerEntry>> = ledgerDao.getAllEntries()

    suspend fun insertExperiment(experiment: ExperimentResult) {
        experimentDao.insertExperiment(experiment)
    }

    suspend fun getExperimentById(id: String): ExperimentResult? {
        return experimentDao.getExperimentById(id)
    }
    
    suspend fun deleteExperimentById(id: String) {
        experimentDao.deleteExperimentById(id)
    }

    suspend fun insertLedgerEntry(entry: LedgerEntry) {
        ledgerDao.insertEntry(entry)
    }
}

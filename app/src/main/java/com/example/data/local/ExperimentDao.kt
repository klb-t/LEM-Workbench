package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ExperimentResult
import kotlinx.coroutines.flow.Flow

@Dao
interface ExperimentDao {
    @Query("SELECT * FROM experiments ORDER BY timestamp DESC")
    fun getAllExperiments(): Flow<List<ExperimentResult>>

    @Query("SELECT * FROM experiments WHERE experimentId = :id")
    suspend fun getExperimentById(id: String): ExperimentResult?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExperiment(experiment: ExperimentResult)
    
    @Query("DELETE FROM experiments WHERE experimentId = :id")
    suspend fun deleteExperimentById(id: String)
}

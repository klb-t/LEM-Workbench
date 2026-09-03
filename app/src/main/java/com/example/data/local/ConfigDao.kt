package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ExperimentConfig
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigDao {
    @Query("SELECT * FROM experiment_configs")
    fun getAllConfigs(): Flow<List<ExperimentConfig>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: ExperimentConfig)
    
    @Query("DELETE FROM experiment_configs WHERE id = :id")
    suspend fun deleteConfigById(id: String)
}

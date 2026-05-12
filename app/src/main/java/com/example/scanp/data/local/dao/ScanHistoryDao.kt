package com.example.scanp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.scanfood.data.local.entities.ScanHistoryEntity

@Dao
interface ScanHistoryDao {
    @Query("SELECT * FROM scan_history_table ORDER BY scannedAt DESC")
    suspend fun getAllHistory(): List<ScanHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(entry: ScanHistoryEntity)
}

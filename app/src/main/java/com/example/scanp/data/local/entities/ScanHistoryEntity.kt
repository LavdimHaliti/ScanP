package com.example.scanp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.scanp.data.domain.ProductDomain

@Entity(tableName = "scan_history_table")
data class ScanHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val barcode: String,
    val scannedAt: Long
)
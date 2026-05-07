package com.example.scanp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scanp.data.local.dao.ProductDao
import com.example.scanp.data.local.dao.ScanHistoryDao
import com.example.scanp.data.local.entities.ProductEntity
import com.example.scanp.data.local.entities.ScanHistoryEntity

/**
 * Room database for the app.
 * Includes tables for products and scan history.
 */
@Database(
    entities = [ProductEntity::class, ScanHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun scanHistoryDao(): ScanHistoryDao

}

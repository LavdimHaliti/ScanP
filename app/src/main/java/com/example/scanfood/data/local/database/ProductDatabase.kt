package com.example.scanfood.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scanfood.data.local.entities.ScanHistoryEntity
import com.example.scanfood.data.local.dao.ProductDao
import com.example.scanfood.data.local.dao.ScanHistoryDao
import com.example.scanfood.data.local.entities.ProductEntity

/**
 * Room database for the app.
 * Includes tables for products and scan history.
 */
@Database(
    entities = [ProductEntity::class, ScanHistoryEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun scanHistoryDao(): ScanHistoryDao

}

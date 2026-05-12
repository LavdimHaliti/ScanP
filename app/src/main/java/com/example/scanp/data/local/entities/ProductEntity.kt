package com.example.scanp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products_table")
data class ProductEntity(
    @PrimaryKey val barcode: String,
    val name: String?,
    val ingredients: String?,
    val energyKcal: Double?,
    val proteins: Double?,
    val carbs: Double?,
    val sugars: Double?,
    val fat: Double?,
    val saturatedFat: Double?,
    val fibre: Double?,
    val salt: Double?,
    val imageUrl: String?
)
package com.example.scanp.data.domain

/**
 * Domain representation of a food product.
 * This model is used throughout the app's business logic and UI layers.
 */

data class ProductDomain(
    val barcode: String,
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
    val isSaved: Boolean = false
)
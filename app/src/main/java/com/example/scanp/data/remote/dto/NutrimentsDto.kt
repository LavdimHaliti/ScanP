package com.example.scanp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NutrimentsDto(
    @SerializedName("energy-kcal")
    val energyKcal: Double?,

    @SerializedName("proteins_100g")
    val proteins: Double?,

    @SerializedName("carbohydrates_100g")
    val carbohydrates: Double?,

    @SerializedName("sugars_100g")
    val sugars: Double?,

    @SerializedName("fat_100g")
    val fat: Double?,

    @SerializedName("saturated-fat_100g")
    val saturatedFat: Double?,

    @SerializedName("fibre_100g")
    val fibre: Double?,

    @SerializedName("salt_100g")
    val salt: Double?
)
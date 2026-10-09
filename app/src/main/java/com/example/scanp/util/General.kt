package com.example.scanp.util

import com.example.scanp.data.domain.ProductDomain
import com.example.scanp.data.local.entities.ScanHistoryEntity

object General {

    fun ProductTypeEnum.hasNutrition(): Boolean = when (this) {
        ProductTypeEnum.FOOD, ProductTypeEnum.PET_FOOD -> true
        ProductTypeEnum.BEAUTY -> false
        ProductTypeEnum.UNKNOWN -> false
    }

    fun ScanHistoryEntity.toDefaultValuesDomain(): ProductDomain {
        return ProductDomain(
            barcode = barcode,
            name = null,
            productType = null,
            ingredients = null,
            energyKcal = null,
            proteins = null,
            carbs = null,
            sugars = null,
            fat = null,
            saturatedFat = null,
            fibre = null,
            salt = null,
            imageUrl = null,
            isSaved = false
        )
    }

}
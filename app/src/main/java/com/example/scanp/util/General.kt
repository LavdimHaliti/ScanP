package com.example.scanp.util

object General {

    fun ProductTypeEnum.hasNutrition(): Boolean = when (this) {
        ProductTypeEnum.FOOD, ProductTypeEnum.PET_FOOD -> true
        ProductTypeEnum.BEAUTY -> false
        ProductTypeEnum.UNKNOWN -> false
    }

}
package com.example.scanp.util

import com.example.scanp.util.Constants.BEAUTY_TYPE
import com.example.scanp.util.Constants.FOOD_TYPE
import com.example.scanp.util.Constants.PETFOOD_TYPE
import com.example.scanp.util.Constants.PRODUCT_TYPE

enum class ProductTypeEnum {
    FOOD,
    BEAUTY,
    PET_FOOD,
    UNKNOWN;

    companion object {
        fun productTypeFromApi(productType: String?): ProductTypeEnum = when (productType?.lowercase()) {
            FOOD_TYPE -> FOOD
            BEAUTY_TYPE -> BEAUTY
            PETFOOD_TYPE -> PET_FOOD
            else -> UNKNOWN
        }
    }

}
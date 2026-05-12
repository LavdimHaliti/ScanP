package com.example.scanp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("product_name")
    val productName: String?,

    @SerializedName("ingredients_text")
    val ingredientsText: String?,

    @SerializedName("image_small_url")
    val imageSmallUrl: String?,

    @SerializedName("nutriments")
    val nutriments: NutrimentsDto?,

    @SerializedName("product_type")
    val productType: String?
)
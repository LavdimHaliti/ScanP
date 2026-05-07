package com.example.scanp.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Wrapper for the Open Food Facts API response.
 */

data class ProductResponseDto(
    @SerializedName("status")
    val status: Int,

    @SerializedName("product")
    val product: ProductDto?
)
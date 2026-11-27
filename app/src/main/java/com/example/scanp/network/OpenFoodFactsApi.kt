package com.example.scanp.network


import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response
import com.example.scanp.data.remote.dto.ProductResponseDto
import com.example.scanp.data.remote.dto.SearchResponseDto
import retrofit2.http.Query

interface OpenFoodFactsApi {
    @GET("api/v3/product/{code}")
    suspend fun getProduct(
        @Path("code") barcode: String,
        @Query("product_type") productType: String = "all"
    ): Response<ProductResponseDto>
}
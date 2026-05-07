package com.example.scanp.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response
import com.example.scanp.data.remote.dto.ProductResponseDto

interface OpenFoodFactsApi {
    @GET("product/{code}")
    suspend fun getProduct(
        @Path("code") barcode: String
    ): Response<ProductResponseDto>
}
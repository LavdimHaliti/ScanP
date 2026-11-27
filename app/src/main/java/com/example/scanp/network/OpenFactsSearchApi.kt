package com.example.scanp.network

import com.example.scanp.data.remote.dto.SearchResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFactsSearchApi {
    @GET("api/v2/search")
    suspend fun searchProduct(
        @Query("brands_tags") brand: String,
        @Query("fields") fields: String = "code,product_name,brands,product_type,ingredients_text,image_small_url",
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): Response<SearchResponseDto>
}
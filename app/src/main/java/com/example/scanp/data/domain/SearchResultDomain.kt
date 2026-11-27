package com.example.scanp.data.domain

data class SearchResultDomain(
    val products: List<ProductDomain>,
    val page: Int,
    val pageCount: Int,
    val totalCount: Int
)

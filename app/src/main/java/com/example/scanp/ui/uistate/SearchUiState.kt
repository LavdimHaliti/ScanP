package com.example.scanp.ui.uistate

import com.example.scanp.data.domain.ProductDomain

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val products: List<ProductDomain> = emptyList(),
    val totalCount: Int = 0,
    val hasSearched: Boolean = false,
    val errorMessage: String? = null
)

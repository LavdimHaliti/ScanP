package com.example.scanp.ui.uistate

import com.example.scanp.data.domain.ProductDomain

data class ProductDetailUiState(
    val product: ProductDomain? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

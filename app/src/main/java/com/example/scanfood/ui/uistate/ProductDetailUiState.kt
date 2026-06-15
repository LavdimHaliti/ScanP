package com.example.scanfood.ui.uistate

import com.example.scanfood.data.domain.ProductDomain

data class ProductDetailUiState(
    val product: ProductDomain? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

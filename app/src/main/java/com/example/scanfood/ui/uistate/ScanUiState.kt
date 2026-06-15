package com.example.scanfood.ui.uistate

import com.example.scanfood.data.domain.ProductDomain

data class ScanUiState(
    val isLoading: Boolean = false,
    val product: ProductDomain? = null,
    val error: String? = null,
    val lastBarcode: String? = null,
    val isNonFoodProduct: Boolean = false
)

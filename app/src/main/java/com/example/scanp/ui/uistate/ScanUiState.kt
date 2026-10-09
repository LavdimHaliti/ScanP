package com.example.scanp.ui.uistate

import com.example.scanp.data.domain.ProductDomain

data class ScanUiState(
    val isLoading: Boolean = false,
    val product: ProductDomain? = null,
    val error: String? = null,
    val lastBarcode: String? = null
)

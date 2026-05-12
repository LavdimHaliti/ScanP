package com.example.scanp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanp.data.repository.ProductRepository
import com.example.scanp.ui.uistate.ProductDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    fun loadProduct(barcode: String) {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState(isLoading = true)
            val result = repository.getProductByBarcode(barcode)
            if (result.isSuccess) {
                _uiState.value = ProductDetailUiState(product = result.getOrNull())
            } else {
                _uiState.value = ProductDetailUiState(
                    error = result.exceptionOrNull()?.message ?: "Failed to load product"
                )
            }
        }
    }
}

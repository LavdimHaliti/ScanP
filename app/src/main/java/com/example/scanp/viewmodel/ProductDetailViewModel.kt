package com.example.scanp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanp.data.repository.ProductRepository
import com.example.scanp.ui.uistate.ProductDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the product detail screen.
 * Manages loading a product by barcode and exposing a reactive UI state.
 *
 * @param repository Source for fetching product data.
 */
class ProductDetailViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    // Internal mutable state holder for UI
    private val _uiState = MutableStateFlow(ProductDetailUiState())
    // Public immutable UI state exposed to the UI layer
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    /**
     * Fetches the product with the specified [barcode] from the repository
     * and updates the UI state accordingly.
     */
    // Load product details for the given barcode and update UI state
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

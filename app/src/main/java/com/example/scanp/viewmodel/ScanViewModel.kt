package com.example.scanp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanp.data.repository.ProductRepository
import com.example.scanp.ui.uistate.ScanUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for barcode scanning operations.
 * It fetches product data from the repository, caches the result locally,
 * records the scan in history, and exposes a reactive UI state.
 *
 * @param repository The data source used to look up products by barcode.
 */
class ScanViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    // Internal mutable state flow holding the current UI state.
    private val _uiState = MutableStateFlow(ScanUiState())

    // Publicly exposed read-only state flow that the UI collects.
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    /**
     * Initiates a product lookup for the given [barcode].
     * Immediately sets the state to loading, then on success saves the product
     * and scan history before updating the UI. On failure the error message is surfaced.
     */
    fun fetchProduct(barcode: String) {
        viewModelScope.launch {
            // Display loading indicator while fetching
            _uiState.value = ScanUiState(isLoading = true, lastBarcode = barcode)

            val result = repository.getProductByBarcode(barcode)
            if (result.isSuccess) {
                // Force unwrap is safe here because isSuccess guarantees a value
                val product = result.getOrNull()!!
                repository.saveProduct(product)
                repository.saveScanHistory(barcode)
                _uiState.value = ScanUiState(product = product, lastBarcode = barcode)
            } else {
                _uiState.value = ScanUiState(
                    error = result.exceptionOrNull()?.message ?: "Unknown error",
                    lastBarcode = barcode
                )
            }
        }
    }

    /** Clears any error currently shown in the UI, leaving other state unchanged. */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /** Resets the entire scan UI state back to its initial empty values. */
    fun resetScan() {
        _uiState.value = ScanUiState()
    }
}
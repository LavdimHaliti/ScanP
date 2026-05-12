package com.example.scanp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanp.data.repository.ProductRepository
import com.example.scanp.ui.uistate.ScanUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScanViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun fetchProduct(barcode: String) {
        viewModelScope.launch {
            _uiState.value = ScanUiState(isLoading = true, lastBarcode = barcode)

            val result = repository.getProductByBarcode(barcode)
            if (result.isSuccess) {
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

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetScan() {
        _uiState.value = ScanUiState()
    }
}
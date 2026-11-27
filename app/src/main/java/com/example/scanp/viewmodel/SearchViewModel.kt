package com.example.scanp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanp.data.repository.ProductRepository
import com.example.scanp.ui.uistate.SearchUiState
import com.example.scanp.util.General.errorUerMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun searchProduct(brand: String) {
        val trimmedBrand = brand.trim()
        if (trimmedBrand.isEmpty()) {
            searchJob?.cancel()
            _uiState.value = SearchUiState()
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val result = repository.searchProduct(brand)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    query = trimmedBrand,
                    isLoading = true,
                    hasSearched = true,
                    errorMessage = null
                )
            }
            repository.searchProduct(trimmedBrand).fold(
                onSuccess = { result ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        products = result.products,
                        totalCount = result.totalCount,
                        errorMessage = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        products = emptyList(),
                        totalCount = 0,
                        errorMessage = error.errorUerMessage()
                    )
                }
            )
        }
    }

    fun updateQuery(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }
}
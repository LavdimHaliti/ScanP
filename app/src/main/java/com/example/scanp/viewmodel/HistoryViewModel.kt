package com.example.scanp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanp.data.repository.ProductRepository
import com.example.scanfood.data.local.entities.ScanHistoryEntity
import com.example.scanp.data.domain.ProductDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel that manages the scan history screen.
 * Loads all previously scanned products from the repository and exposes them as a reactive list.
 *
 * @param repository The data source used to retrieve persisted scan history records.
 */
class HistoryViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    // Internal mutable state flow holding the history list.
    private val _history = MutableStateFlow<List<ProductDomain>>(emptyList())

    // Publicly exposed read-only state flow that the UI collects to display history entries.
    val history: StateFlow<List<ProductDomain>> = _history.asStateFlow()

    init {
        // Load the full scan history as soon as the ViewModel is created.
        viewModelScope.launch {
            _history.value = repository.getAllHistory()
        }
    }
}
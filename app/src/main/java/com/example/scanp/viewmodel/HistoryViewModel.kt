package com.example.scanp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanp.data.repository.ProductRepository
import com.example.scanp.data.domain.ProductDomain
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel that manages the scan history screen.
 * Loads all previously scanned products from the repository and exposes them as a reactive list.
 *
 * @param repository The data source used to retrieve persisted scan history records.
 */
class HistoryViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    // Publicly exposed read-only state flow that the UI collects to display history entries.
    val history: StateFlow<List<ProductDomain>> = repository.getAllHistory()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

}
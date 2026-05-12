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

class HistoryViewModel(
    private val repository: ProductRepository
) : ViewModel() {
    private val _history = MutableStateFlow<List<ProductDomain>>(emptyList())
    val history: StateFlow<List<ProductDomain>> = _history.asStateFlow()

    init {
        viewModelScope.launch {
            _history.value = repository.getAllHistory()
        }
    }
}
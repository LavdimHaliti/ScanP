package com.example.scanp.data.repository

import com.example.scanp.data.local.dao.ProductDao
import com.example.scanp.data.local.dao.ScanHistoryDao
import com.example.scanp.network.OpenFoodFactsApi

class ProductRepositoryImpl(
    private val api: OpenFoodFactsApi,
    private val productDao: ProductDao,
    private val scanHistoryDao: ScanHistoryDao
) : ProductRepository {
    //TODO
}
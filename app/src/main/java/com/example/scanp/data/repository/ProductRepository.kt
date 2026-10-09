package com.example.scanp.data.repository

import com.example.scanp.data.domain.ProductDomain
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductByBarcode(barcode: String): Result<ProductDomain>
    suspend fun saveProduct(product: ProductDomain)
    suspend fun saveScanHistory(barcode: String)
    fun getAllHistory(): Flow<List<ProductDomain>>
}
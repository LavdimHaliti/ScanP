package com.example.scanfood.data.repository

import com.example.scanfood.data.domain.ProductDomain

interface ProductRepository {
    suspend fun getProductByBarcode(barcode: String): Result<ProductDomain>
    suspend fun saveProduct(product: ProductDomain)
    suspend fun saveScanHistory(barcode: String)
    suspend fun getAllHistory(): List<ProductDomain>
}
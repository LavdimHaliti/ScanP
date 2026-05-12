package com.example.scanp.data.repository

import com.example.scanfood.data.local.entities.ScanHistoryEntity
import com.example.scanp.data.domain.ProductDomain
import com.example.scanp.data.local.dao.ProductDao
import com.example.scanp.data.local.dao.ScanHistoryDao
import com.example.scanp.data.mapper.toProductDomain
import com.example.scanp.data.mapper.toProductEntity
import com.example.scanp.network.OpenFoodFactsApi

class ProductRepositoryImpl(
    private val api: OpenFoodFactsApi,
    private val productDao: ProductDao,
    private val scanHistoryDao: ScanHistoryDao
) : ProductRepository {

    override suspend fun getProductByBarcode(barcode: String): Result<ProductDomain> {
        return try {
            val response = api.getProduct(barcode)
            if (response.isSuccessful && response.body() != null) {
                val productDto = response.body()!!.product
                if (productDto != null) {
                    val domain = productDto.toProductDomain(barcode)
                    Result.success(domain)
                } else {
                    Result.failure(Exception("Product not found"))
                }
            } else {
                Result.failure(Exception("API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveProduct(product: ProductDomain) {
        productDao.insertProduct(product.toProductEntity())
    }

    override suspend fun saveScanHistory(barcode: String) {
        scanHistoryDao.insertHistory(
            ScanHistoryEntity(barcode = barcode, scannedAt = System.currentTimeMillis())
        )
    }

    override suspend fun getAllHistory(): List<ProductDomain> {
        val histories = scanHistoryDao.getAllHistory().distinctBy { it.barcode }
        return histories.map { entry ->
            val productEntity = productDao.getProduct(entry.barcode)
            productEntity?.
            toProductDomain(isSaved = true)
                ?:
                ProductDomain(
                    barcode = entry.barcode,
                    name = null,
                    ingredients = null,
                    energyKcal = null,
                    proteins = null,
                    carbs = null,
                    sugars = null,
                    fat = null,
                    saturatedFat = null,
                    fibre = null,
                    salt = null,
                    imageUrl = null,
                    isSaved = false
                )
        }
    }
}
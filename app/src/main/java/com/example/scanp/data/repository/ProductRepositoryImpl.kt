package com.example.scanp.data.repository

import com.example.scanfood.data.local.entities.ScanHistoryEntity
import com.example.scanp.data.domain.ProductDomain
import com.example.scanp.data.local.dao.ProductDao
import com.example.scanp.data.local.dao.ScanHistoryDao
import com.example.scanp.data.mapper.toProductDomain
import com.example.scanp.data.mapper.toProductEntity
import com.example.scanp.network.OpenFoodFactsApi

/**
 * Concrete implementation of [ProductRepository] that fetches product data
 * from the Open Food Facts API and persists products and scan history locally.
 */
class ProductRepositoryImpl(
    private val api: OpenFoodFactsApi,
    private val productDao: ProductDao,
    private val scanHistoryDao: ScanHistoryDao
) : ProductRepository {

    /**
     * Retrieves a product by barcode from the remote API.
     * Maps the successful response into a [ProductDomain], or returns a failure
     * with an appropriate error message.
     */
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

    /** Persists a product in the local database. */
    override suspend fun saveProduct(product: ProductDomain) {
        productDao.insertProduct(product.toProductEntity())
    }

    /** Records a scan event for the given barcode with the current timestamp. */
    override suspend fun saveScanHistory(barcode: String) {
        scanHistoryDao.insertHistory(
            ScanHistoryEntity(barcode = barcode, scannedAt = System.currentTimeMillis())
        )
    }

    /**
     * Returns a deduplicated list of scanned products (by barcode), ordered
     * by most recent scan first. If a product entity is missing locally, a
     * placeholder with only the barcode is created.
     */
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
package com.example.scanp.data.repository

import com.example.scanp.data.local.entities.ScanHistoryEntity
import com.example.scanp.data.domain.ProductDomain
import com.example.scanp.data.local.dao.ProductDao
import com.example.scanp.data.local.dao.ScanHistoryDao
import com.example.scanp.data.mapper.toProductDomain
import com.example.scanp.data.mapper.toProductEntity
import com.example.scanp.network.OpenFoodFactsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
            when {
                response.isSuccessful && response.body() != null -> {
                    val productDto = response.body()!!.product
                    val domain = productDto!!.toProductDomain(barcode)
                    Result.success(domain)
                }
                response.code() == 404 -> {
                    Result.failure(Exception("Product not found"))
                }
                else -> {
                    Result.failure(Exception("API Error: ${response.code()}"))
                }
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
    override fun getAllHistory(): Flow<List<ProductDomain>> {
        val productHistories = scanHistoryDao.getAllHistory().map { scanHistoryList -> scanHistoryList.distinctBy { it.barcode } }
        return productHistories.map { productHistoryList ->
            productHistoryList.map { product ->
                val productEntity = productDao.getProduct(product.barcode)
                productEntity?.toProductDomain(isSaved = true)
                    ?: ProductDomain(
                        barcode = product.barcode,
                        name = null,
                        productType = null,
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
}
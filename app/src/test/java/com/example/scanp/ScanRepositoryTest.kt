package com.example.scanp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.scanp.data.domain.ProductDomain
import com.example.scanp.data.local.dao.ProductDao
import com.example.scanp.data.local.dao.ScanHistoryDao
import com.example.scanp.data.local.database.ProductDatabase
import com.example.scanp.data.local.entities.ProductEntity
import com.example.scanp.data.local.entities.ScanHistoryEntity
import com.example.scanp.data.remote.dto.NutrimentsDto
import com.example.scanp.data.remote.dto.ProductDto
import com.example.scanp.data.remote.dto.ProductResponseDto
import com.example.scanp.data.repository.ProductRepositoryImpl
import com.example.scanp.network.OpenFoodFactsApi
import com.example.scanp.util.ApiHttpException
import com.example.scanp.util.ProductNotFoundException
import com.example.scanp.util.ProductTypeEnum
import com.google.android.gms.common.api.ApiException
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.bouncycastle.jcajce.provider.asymmetric.CONTEXT
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Response

@RunWith(RobolectricTestRunner::class)
class ScanRepositoryTest {

    private lateinit var database: ProductDatabase
    private lateinit var productDao: ProductDao
    private lateinit var historyDao: ScanHistoryDao
    private lateinit var api: OpenFoodFactsApi
    private lateinit var repository: ProductRepositoryImpl

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, ProductDatabase::class.java).allowMainThreadQueries().build()

        productDao = database.productDao()
        historyDao = database.scanHistoryDao()
        api = mockk(relaxed = true)

        repository = ProductRepositoryImpl(api, productDao, historyDao)
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun `getProductByBarcode returns success on valid response`() = runTest {
        val barcode = "0800897003968"
        coEvery {  api.getProduct(barcode) } returns createMockSuccessfulResponse("food")

        val result = repository.getProductByBarcode(barcode)

        assertTrue(result.isSuccess)
        val product = result.getOrNull()
        assertNotNull(product)
        assertEquals(barcode, product?.barcode)
        assertEquals("Test Product", product?.name)
        assertEquals(ProductTypeEnum.FOOD, product?.productType)
        assertEquals("Water, Sugar, Salt", product?.ingredients)
        assertEquals(100.0, product?.energyKcal)
        assertEquals(5.0, product?.proteins)
    }

    @Test
    fun `getProductByBarcode returns error 404`() = runTest {
        val barcode = "9999999999999"
        coEvery { api.getProduct(barcode) } returns createMockNotFoundResponse(404)

        val result = repository.getProductByBarcode(barcode)

        assertTrue(result.isFailure)
        assertTrue("Expected ProductNotFoundException but got ${result.exceptionOrNull()}",
            result.exceptionOrNull() is ProductNotFoundException)
    }

    @Test
    fun `getProductByBarcode returns error 500`() = runTest {
        val barcode = "0800897003968"
        coEvery { api.getProduct(barcode) } returns createMockNotFoundResponse(500)

        val result = repository.getProductByBarcode(barcode)

        assertTrue(result.isFailure)
        assertTrue("Expected ApiException but got ${result.exceptionOrNull()}",
            result.exceptionOrNull() is ApiHttpException)
        assertEquals(500, (result.exceptionOrNull() as ApiHttpException).code)
    }

    @Test
    fun `saveProduct saves product to database`() = runTest {
        val barcode = "0800897003968"
        val product = createTestProduct(barcode)

        repository.saveProduct(product)

        val savedProduct = productDao.getProduct(barcode)
        assertNotNull(savedProduct)
        assertEquals(product.name, savedProduct?.name)
    }

    @Test
    fun `saveScanHistory saves scanned product`() = runTest {
        val barcode = "0800897003968"

        repository.saveScanHistory(barcode)

        historyDao.getAllHistory().test {
            val historyLIst = awaitItem()
            assertEquals(1, historyLIst.size)
            assertEquals(barcode, historyLIst[0].barcode)
            assertTrue(historyLIst[0].scannedAt > 0L)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAllHistory returns deduplicated products with matching product names`() = runTest {
        historyDao.insertHistory(ScanHistoryEntity(1L, "111", scannedAt = 1000L))
        historyDao.insertHistory(ScanHistoryEntity(2L, "222", scannedAt = 2000L))
        historyDao.insertHistory(ScanHistoryEntity(3L, "111", scannedAt = 3000L))

        productDao.insertProduct(createProductEntity("111", "Product one"))
        productDao.insertProduct(createProductEntity("222", "Product two"))

        repository.getAllHistory().test {
            val historyList = awaitItem()

            assertEquals(2, historyList.size)

            assertEquals("111", historyList[0].barcode)
            assertEquals("Product one", historyList[0].name)

            assertEquals("222", historyList[1].barcode)
            assertEquals("Product two", historyList[1].name)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createNutrimentsDto(
        energyKcal: Double = 100.0,
        proteins: Double = 5.0,
        carbohydrates: Double = 20.0
    ) = NutrimentsDto(
        energyKcal = energyKcal,
        proteins = proteins,
        carbohydrates = carbohydrates,
        sugars = 10.0,
        fat = 3.0,
        saturatedFat = 1.0,
        fibre = 2.0,
        salt = 0.5
    )

    private fun createProductDto(
        productName: String = "Test Product",
        ingredientsText: String = "Water, Sugar, Salt",
        productType: String = "food"
    ) = ProductDto(
        productName = productName,
        ingredientsText = ingredientsText,
        imageSmallUrl = null,
        nutriments = createNutrimentsDto(),
        productType = productType
    )

    private fun createMockSuccessfulResponse(
        productType: String = "food"
    ): Response<ProductResponseDto> = Response.success(
        ProductResponseDto(
            status = "success",
            product = createProductDto(productType = productType)
        )
    )

    private fun createMockNotFoundResponse(code: Int): Response<ProductResponseDto> =
        Response.error(
            code,
            "".toResponseBody("application/json".toMediaType())
        )

    private fun createTestProduct(barcode: String): ProductDomain {
        return ProductDomain(
            barcode = barcode,
            name = "Sample Product",
            productType = null,
            ingredients = "Water, Sugar, Salt, Citric Acid",
            energyKcal = 1.00,
            proteins = 1.00,
            carbs = 1.00,
            sugars = 1.00,
            fat = 1.00,
            saturatedFat = 1.00,
            fibre = 1.00,
            salt = 1.00,
            imageUrl = null,
            isSaved = false
        )
    }

    private fun createProductEntity(barcode: String, name: String?) = ProductEntity(
        barcode = barcode,
        name = name,
        productType = "food",
        ingredients = null,
        energyKcal = null,
        proteins = null,
        carbs = null,
        sugars = null,
        fat = null,
        saturatedFat = null,
        fibre = null,
        salt = null,
        imageUrl = null
    )

}
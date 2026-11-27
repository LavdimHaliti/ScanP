package com.example.scanp

import app.cash.turbine.test
import com.example.scanp.data.domain.ProductDomain
import com.example.scanp.data.domain.SearchResultDomain
import com.example.scanp.data.repository.ProductRepository
import com.example.scanp.di.testModule
import com.example.scanp.viewmodel.SearchViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class SearchViewModelTest {

    private lateinit var repository: ProductRepository
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        startKoin { modules(testModule) }
        repository = mockk(relaxed = true)
        viewModel = SearchViewModel(repository)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `searchProduct success on product search` () = runTest {
        val searchedProduct = "pantene"
        val dummyProducts = createDummyProducts(searchedProduct)
        val gainedProducts = createTestProducts(searchedProduct)
        coEvery { repository.searchProduct(searchedProduct) } returns Result.success(gainedProducts)

        viewModel.uiState.test {
            awaitItem()

            viewModel.searchProduct(searchedProduct)

            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(dummyProducts, successState.products)
            assertNull(successState.errorMessage)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchProduct error on product search` () = runTest {
        val searchedProduct = "pantene"
        coEvery { repository.searchProduct(searchedProduct) } returns Result.failure(Exception("No products found for $searchedProduct"))

        viewModel.uiState.test {
            awaitItem()

            viewModel.searchProduct(searchedProduct)

            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertEquals("No products found for $searchedProduct", errorState.errorMessage)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createDummyProducts(searchedProduct: String): List<ProductDomain> {
        val products = listOf(ProductDomain(
            barcode = "1234567890",
            name = searchedProduct,
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
        ),
            ProductDomain(
                barcode = "0987654321",
                name = searchedProduct,
                productType = null,
                ingredients = "",
                energyKcal = 0.0,
                proteins = 0.0,
                carbs = 0.0,
                sugars = 0.0,
                fat = 0.0,
                saturatedFat = 0.0,
                fibre = 0.0,
                salt = 0.0,
                imageUrl = null,
                isSaved = false
            )
        )
        return products
    }

    private fun createTestProducts(searchedProduct: String): SearchResultDomain {
        val dummyProducts = createDummyProducts(searchedProduct)

        return SearchResultDomain(
            products = dummyProducts,
            page = 1,
            pageCount = 20,
            totalCount = 40
        )
    }

}
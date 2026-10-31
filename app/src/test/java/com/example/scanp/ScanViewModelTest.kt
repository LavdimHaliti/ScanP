package com.example.scanp

import app.cash.turbine.Turbine
import app.cash.turbine.awaitItem
import app.cash.turbine.test
import app.cash.turbine.testIn
import com.example.scanp.data.domain.ProductDomain
import com.example.scanp.data.repository.ProductRepository
import com.example.scanp.di.testModule
import com.example.scanp.ui.uistate.ScanUiState
import com.example.scanp.viewmodel.ScanViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class ScanViewModelTest {

    private lateinit var repository: ProductRepository
    private lateinit var viewModel: ScanViewModel

    // SETUP - Runs BEFORE each test
    @Before
    fun setup() {
        // Create mocks, initialize ViewModel
        startKoin { modules(testModule) }
        repository = mockk(relaxed = true)
        viewModel = ScanViewModel(repository)
    }

    // CLEANUP - Runs AFTER each test
    @After
    fun tearDown() {
        stopKoin()
    }

    // THE TEST CASES - What you're verifying

    // Success Test
    @Test
    fun `fetchProduct emits loading then success` () = runTest {
        // Given test data
        val barcode = "0800897003968"
        val product = createTestProduct(barcode)
        coEvery { repository.getProductByBarcode(barcode) } returns Result.success(product)

        // Then verify the fetched data
        viewModel.uiState.test {
            awaitItem() // skip initial idle state

            viewModel.fetchProduct(barcode)

            // Assert data
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(product, successState.product)
            assertNull(successState.error)

            cancelAndIgnoreRemainingEvents() // StateFlow never completes
        }

    }

    //Failure Test
    @Test
    fun `fetchProduct emits loading then error on failure`() = runTest {
        val barcode = "0800897003968"
        coEvery { repository.getProductByBarcode(barcode) } returns Result.failure(Exception("Network Error"))

        viewModel.uiState.test {
            awaitItem()

            viewModel.fetchProduct(barcode)
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val failureState = awaitItem()
            assertFalse(failureState.isLoading)
            assertEquals("Network Error", failureState.error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // Error creating Test
    @Test
    fun `clearError removes error`() = runTest {

        coEvery { repository.getProductByBarcode(any()) } returns Result.failure(Exception("Error"))
        viewModel.uiState.test {
            awaitItem()

            // Create an error
            viewModel.fetchProduct("123")

            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            // Assert and check error message is not null
            val errorState = awaitItem()
            assertNotNull(errorState.error)
            assertFalse(errorState.isLoading)

            // Clear error
            viewModel.clearError()

            val clearedState = awaitItem()
            assertNull(clearedState.error)

            cancelAndIgnoreRemainingEvents()

        }
    }

    // Reset scan test on success
    @Test
    fun `resetScan resets state to initial state on success`() = runTest {
        val barcode = "0800897003968"
        val product = createTestProduct(barcode)

        coEvery { repository.getProductByBarcode(barcode) } returns Result.success(createTestProduct(barcode))

        viewModel.uiState.test {
            awaitItem()

            viewModel.fetchProduct(barcode)

            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(product, successState.product)
            assertNull(successState.error)

            viewModel.resetScan()

            cancelAndIgnoreRemainingEvents()
        }
    }

    // Reset scan test on error
    @Test
    fun `resetScan resets state to initial state on error`() = runTest {
        val barcode = "0800897003968"

        coEvery { repository.getProductByBarcode(any()) } returns Result.failure(Exception("Network Error"))

        viewModel.uiState.test {
            awaitItem()

            viewModel.fetchProduct(barcode)

            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertEquals("Network Error", errorState.error)
            assertNotNull(errorState.error)

            viewModel.resetScan()

            cancelAndIgnoreRemainingEvents()
        }
    }

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
}

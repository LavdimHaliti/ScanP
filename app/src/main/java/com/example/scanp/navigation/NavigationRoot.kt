package com.example.scanp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.scanp.ui.history.HistoryScreen
import com.example.scanp.ui.ingredient.IngredientListScreen
import com.example.scanp.ui.nutrition.NutritionDetailScreen
import com.example.scanp.ui.scan.ScanScreen
import com.example.scanp.viewmodel.HistoryViewModel
import com.example.scanp.viewmodel.ProductDetailViewModel
import com.example.scanp.viewmodel.ScanViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(Route.ScanScreen)

    val scanViewModel: ScanViewModel = koinViewModel()
    val scanHistoryViewModel: HistoryViewModel = koinViewModel()
    val productDetailViewModel: ProductDetailViewModel = koinViewModel()

    val scanUiState by scanViewModel.uiState.collectAsStateWithLifecycle()
    val scanHistory by scanHistoryViewModel.history.collectAsStateWithLifecycle()
    val productDetailUiState by productDetailViewModel.uiState.collectAsStateWithLifecycle()

    NavDisplay(
        backStack = backStack,
        entryProvider = { key ->
            when (key) {
                is Route.ScanScreen ->  {
                    NavEntry(key) {
                        ScanScreen(
                            uiState = scanUiState,
                            navigateToHistory = {
                                backStack.add(Route.HistoryScreen)
                            },
                            navigateToNutrition = { barcode ->
                                backStack.add(Route.NutritionDetailScreen(barcode))
                            },
                            navigateToIngredientList = { barcode ->
                                backStack.add(Route.IngredientListScreen(barcode))
                            },
                            fetchProduct = { barcode -> scanViewModel.fetchProduct(barcode) },
                            resetScan = { scanViewModel.resetScan() },
                            clearError = { scanViewModel.clearError() }
                        )
                    }
                }

                is Route.HistoryScreen -> {
                    NavEntry(key) {
                        HistoryScreen(
                            history = scanHistory,
                            onItemClick = { barcode -> backStack.add(Route.IngredientListScreen(barcode)) },
                            onNavigateBack = { backStack.removeLastOrNull() }
                        )
                    }
                }

                is Route.IngredientListScreen -> {
                    NavEntry(key) {
                        IngredientListScreen(
                            uiState = productDetailUiState,
                            barcode = key.barcode,
                            onNavigateBack = { backStack.removeLastOrNull() },
                            loadProduct = { barcode -> productDetailViewModel.loadProduct(barcode) }
                        )
                    }
                }

                is Route.NutritionDetailScreen -> {
                    NavEntry(key) {
                        NutritionDetailScreen(
                            uiState = productDetailUiState,
                            barcode = key.barcode,
                            onNavigateBack = { backStack.removeLastOrNull() },
                            loadProduct = { barcode -> productDetailViewModel.loadProduct(barcode) }
                        )
                    }
                }

                else -> error("Unknown Route: $key")
            }
        }
    )
}
package com.example.scanfood.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.scanfood.ui.history.HistoryScreen
import com.example.scanfood.ui.ingredient.IngredientListScreen
import com.example.scanfood.ui.nutrition.NutritionDetailScreen
import com.example.scanfood.ui.scan.ScanScreen
import com.example.scanfood.viewmodel.HistoryViewModel
import com.example.scanfood.viewmodel.ProductDetailViewModel
import com.example.scanfood.viewmodel.ScanViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(Route.ScanScreen)

    val scanViewModel: ScanViewModel = koinViewModel()
    val scanHistoryViewModel: HistoryViewModel = koinViewModel()
    val productDetailViewModel: ProductDetailViewModel = koinViewModel()

    NavDisplay(
        backStack = backStack,
        entryProvider = { key ->
            when (key) {
                is Route.ScanScreen ->  {
                    NavEntry(key) {
                        ScanScreen(
                            viewModel = scanViewModel,
                            navigateToHistory = {
                                backStack.add(Route.HistoryScreen)
                            },
                            navigateToNutrition = { barcode ->
                                backStack.add(Route.NutritionDetailScreen(barcode))
                            },
                            navigateToIngredientList = { barcode ->
                                backStack.add(Route.IngredientListScreen(barcode))
                            }
                        )
                    }
                }

                is Route.HistoryScreen -> {
                    NavEntry(key) {
                        HistoryScreen(
                            viewModel = scanHistoryViewModel,
                            onItemClick = { barcode -> backStack.add(Route.IngredientListScreen(barcode)) },
                            onNavigateBack = { backStack.removeLastOrNull() }
                        )
                    }
                }

                is Route.IngredientListScreen -> {
                    NavEntry(key) {
                        IngredientListScreen(
                            viewModel = productDetailViewModel,
                            barcode = key.barcode,
                            onNavigateBack = { backStack.removeLastOrNull() }
                        )
                    }
                }

                is Route.NutritionDetailScreen -> {
                    NavEntry(key) {
                        NutritionDetailScreen(
                            viewModel = productDetailViewModel,
                            barcode = key.barcode,
                            onNavigateBack = { backStack.removeLastOrNull() }
                        )
                    }
                }

                else -> error("Unknown Route: $key")
            }
        }
    )
}
package com.example.scanfood.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object ScanScreen: Route

    @Serializable
    data class IngredientListScreen(val barcode: String): Route, NavKey

    @Serializable
    data class NutritionDetailScreen(val barcode: String): Route, NavKey

    @Serializable
    data object HistoryScreen: Route, NavKey
    companion object {
    }
}
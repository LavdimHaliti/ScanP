package com.example.scanp.navigation

sealed class Screen(val route: String) {
    object Scan : Screen("scan")

    object IngredientList: Screen("ingredient/{barcode}") {
        fun createRoute(barcode: String) = "ingredient/$barcode"
    }

    object Nutrition : Screen("nutrition/barcode") {
        fun createRoute(barcode: String) = "nutrition/$barcode"
    }

    object History : Screen("history")
}
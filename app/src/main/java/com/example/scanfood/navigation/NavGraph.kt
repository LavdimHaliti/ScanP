package com.example.scanfood.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scanfood.ui.history.HistoryScreen
import com.example.scanfood.ui.ingredient.IngredientListScreen
import com.example.scanfood.ui.nutrition.NutritionDetailScreen
import com.example.scanfood.ui.scan.ScanScreen

@Composable
fun ScanFoodApp(navController: NavHostController = rememberNavController()) {
    ScanFoodNavGraph(navController = navController)
}

@Composable
fun ScanFoodNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Scan.route
    ) {
        composable(route = Screen.Scan.route) {
            ScanScreen(
                navigateToNutrition = { barcode -> navController.navigate(Screen.Nutrition.createRoute(barcode)) },
                navigateToIngredientList = { barcode -> navController.navigate(Screen.IngredientList.createRoute(barcode)) },
                navigateToHistory = { navController.navigate(Screen.History.route) }
            )
        }

        composable(route = Screen.IngredientList.route) { backStackEntry ->
            val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
            IngredientListScreen(
                barcode = barcode,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Nutrition.route) { backStackEntry ->
            val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
            NutritionDetailScreen(
                barcode = barcode,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.History.route) {
            HistoryScreen(
                onItemClick = { barcode -> navController.navigate(Screen.IngredientList.createRoute(barcode)) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}


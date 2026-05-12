package com.example.scanp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scanp.ui.history.HistoryScreen
import com.example.scanp.ui.ingredient.IngredientListScreen
import com.example.scanp.ui.nutrition.NutritionDetailScreen
import com.example.scanp.ui.scan.ScanScreen

@Composable
fun ScanPApp(navController: NavHostController = rememberNavController()) {
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
            HistoryScreen()
        }
    }
}


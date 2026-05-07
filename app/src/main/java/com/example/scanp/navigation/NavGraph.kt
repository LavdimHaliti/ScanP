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
            ScanScreen()
        }

        composable(route = Screen.IngredientList.route) { backStackEntry ->
            IngredientListScreen()
        }

        composable(route = Screen.Nutrition.route) { backStackEntry ->
            NutritionDetailScreen()
        }

        composable(route = Screen.History.route) {
            HistoryScreen()
        }
    }
}


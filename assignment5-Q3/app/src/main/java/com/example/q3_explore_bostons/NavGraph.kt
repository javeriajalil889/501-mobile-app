package com.example.q3_explore_bostons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Home Screen
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController, modifier = Modifier)
        }
        // Categories Screen
        composable(route = Screen.Categories.route) {
            // FIX 2: And here
            CategoriesScreen(navController = navController, modifier = Modifier)
        }
        // List Screen
        composable(
            route = "list/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            ListScreen(
                // FIX 2: And here
                navController = navController,
                category = backStackEntry.arguments?.getString("category")
            )
        }
        // Details Screen
        composable(
            route = "details/{placeId}",
            arguments = listOf(navArgument("placeId") { type = NavType.IntType })
        ) { backStackEntry ->
            DetailScreen(
                placeId = backStackEntry.arguments?.getInt("placeId")
            )
        }
    }
}

package com.example.q3_explore_bostons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun NavGraph(navControlller: NavHostController){
    NavHost(
        navController = navControlller,
        startDestination = Screen.Home.route
    ){
        //Home Screen
        composable(route = Screen.Home.route){
            HomeScreen(navController = navControlller, modifier = Modifier)
        }
        //Categories Screen
        composable(route=Screen.Categories.route){
            CategoriesScreen(navController = navControlller, modifier = Modifier)
        }
        //List Screen

        //Details Screen

    }
}
package com.example.whats_for_dinner

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.whats_for_dinner.ui.theme.Whats_For_DinnerTheme
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

private const val TAG = "Navigation Demo"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Whats_For_DinnerTheme {
                Surface(
                    modifier=Modifier.fillMaxSize(),
                    color=MaterialTheme.colorScheme.background
                ){
                    App()
                }
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun App() {
    //rememberNavController - creates and remembers the NavController across recompositions
    //keeps track of back stack of composable that make app screens
    val navController = rememberNavController()
    //log the back stack when it changes, automatically updates when back stack changes
    LaunchedEffect(navController) {
        navController.currentBackStack.collect { backStackEntries ->
            val routeList = backStackEntries.joinToString(separator = " -> ") { it.destination.route ?: "null" }
            Log.d(TAG, "Current Back Stack: $routeList")
        }
    }

    //This is where navigation happens, NavHost is a composable that displays other composables
    //based on the current destination based on a path , acts as nav graph for your app
    NavHost(
        navController= navController, // controller manages navigation
        startDestination = "home"   // the route of the first screen to be displayed
    ){
        composable("home"){
            HomeScreen(navController=navController)
        }
        composable ("details"){
            DetailsScreen(navController=navController)
        }
        composable("recipe"){
            RecipeScreen(navController=navController)
        }
    }

}

/*
HomeScreen()- this will represent a composable showing the Home Screen
argument: navController, the controller used to navigate other screens
 */
@Composable
fun HomeScreen(navController:NavController) {
    Column(
        modifier=Modifier.fillMaxSize(),
        verticalArrangement=Arrangement.Center,
        horizontalAlignment=Alignment.CenterHorizontally
    ){
        Text("Home Screen", style= MaterialTheme.typography.headlineMedium)
        Spacer(modifier=Modifier.height(16.dp))
        Button(
            onClick={
                Log.d(TAG, "Navigating from Home to Details....")
                navController.navigate("details")
            }
        ){
            Text("Go to Details Screen")

        }

    }
}
/*
DetailsScreen()- this will represent a composable showing the Details Screen
argument: navController, the controller used to navigate other screens
 */
@Composable
fun DetailsScreen(navController:NavController) {
    Column(
        modifier=Modifier.fillMaxSize(),
        verticalArrangement=Arrangement.Center,
        horizontalAlignment=Alignment.CenterHorizontally
    ){
        Text("Details Screen", style= MaterialTheme.typography.headlineMedium)
        Spacer(modifier=Modifier.height(16.dp))
        Button(
            onClick={
                //to go back up, we call navController.navigateUp(), this pops current screen (DetailsScreen)
                //off the back stack, taking user to back screen (HomeScreen). This method is part of NavController
                //class , no special imports needed
                Log.d(TAG, "Navigate back from Details to Home....")
                navController.navigateUp()
            }
        ){
            Text("Go Back")

        }

    }
}

@Composable
fun RecipeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Recipe Screen", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick={
            Log.d(TAG, "Navigating back from Recipe Screen to Home...")
            navController.navigateUp()
        }
        ){
            Text("Go Back")

        }

    }
}




@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Whats_For_DinnerTheme {
        HomeScreen(navController = rememberNavController())
    }
}


@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    Whats_For_DinnerTheme {
        DetailsScreen(navController = rememberNavController())
    }
}


@Preview(showBackground = true)
@Composable
fun RecipeScreenPreview() {
    Whats_For_DinnerTheme {
        RecipeScreen(navController = rememberNavController())
    }
}
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.clickable import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


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

val recipes = listOf(
    Recipe(1, "Chicken Shawarma", listOf("Chicken", "Pita", "Garlic Sauce"), listOf("Marinate chicken", "Cook chicken", "Assemble shawarma")),
    Recipe(2, "Sweet Potato Gnocchi", listOf("Sweet Potato", "Flour", "Egg"), listOf("Bake potatoes", "Mix dough", "Boil gnocchi")),
    Recipe(3, "Spaghetti Bolognese", listOf("Spaghetti", "Ground Beef", "Tomato Sauce"), listOf("Make the sauce", "Cook spaghetti", "Combine and serve"))
)
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

    //This is where navigation happens, NavHost is a composable that displays other composable
    //based on the current destination based on a path , acts as nav graph for your app
    NavHost(
        navController= navController, // controller manages navigation
        startDestination = "home"   // the route of the first screen to be displayed
    ){
        composable("home"){
            HomeScreen(navController=navController, recipes= recipes)
        }
        composable ("details/{recipeId}"){ backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull()
            val recipe= recipes.find{it.id == recipeId}
            if(recipe != null){
                DetailsScreen(navController=navController, recipe=recipe)
            }
            else{
                Text("Recipe not found")
            }
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
fun HomeScreen(navController: NavController, recipes: List<Recipe>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Home Screen", style = MaterialTheme.typography.headlineMedium)
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
        ) {
            items(recipes) { recipe ->
                // Pass the recipe and an onClick lambda
                RecipeItem(
                    recipe = recipe,
                    onClick = {
                        // Navigate to the details screen with the specific recipe's id
                        navController.navigate("details/${recipe.id}")
                    }
                )
            }
        }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    Log.d(TAG, "Navigating from Home to Details....")
                }
            ) {
                Text("Go to Details Screen")

            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                Log.d(TAG, "Navigating from Home to Recipe....")
                navController.navigate("recipe")
            }
            ) {
                Text("Go to Recipe Screen")
            }

        }
    }
@Composable
fun RecipeItem(recipe: Recipe, onClick: () -> Unit) {     // Each item in the list
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) { // Regular content lambda for Card
        Text(
            text = recipe.title,
            modifier = Modifier
                .padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}




/*
DetailsScreen()- this will represent a composable showing the Details Screen
argument: navController, the controller used to navigate other screens
 */


@Composable
fun DetailsScreen(navController: NavController, recipe: Recipe) { // Updated parameter
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Make screen scrollable
        horizontalAlignment = Alignment.Start // Align content to the start
    ) {
        // Display recipe details
        Text(recipe.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Ingredients:", style = MaterialTheme.typography.titleLarge)
        recipe.ingredients.forEach { ingredient ->
            Text("• $ingredient", modifier = Modifier.padding(start = 8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Steps:", style = MaterialTheme.typography.titleLarge)
        recipe.steps.forEachIndexed { index, step ->
            Text("${index + 1}. $step", modifier = Modifier.padding(start = 8.dp))
        }
        Spacer(modifier = Modifier.height(32.dp))

        // Center the button
        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
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
        HomeScreen(navController = rememberNavController(), recipes= recipes)
    }
}


@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    Whats_For_DinnerTheme {
        DetailsScreen(navController = rememberNavController(), recipes.first())
    }
}


@Preview(showBackground = true)
@Composable
fun RecipeScreenPreview() {
    Whats_For_DinnerTheme {
        RecipeScreen(navController = rememberNavController())
    }
}
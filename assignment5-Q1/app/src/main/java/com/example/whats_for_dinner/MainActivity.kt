package com.example.whats_for_dinner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.whats_for_dinner.ui.theme.Whats_For_DinnerTheme

private const val TAG = "Navigation Demo"

//  sealed class for clear and type-safe routes
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object AddRecipe : Screen("add_recipe", "Add", Icons.Default.Add)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

// List for Bottom Navigation items
val bottomNavItems = listOf(Screen.Home, Screen.AddRecipe, Screen.Settings)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Whats_For_DinnerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

val initialRecipes = listOf(
    Recipe(1, "Chicken Shawarma", listOf("Chicken", "Pita", "Garlic Sauce"), listOf("Marinate chicken", "Cook chicken", "Assemble shawarma")),
    Recipe(2, "Sweet Potato Gnocchi", listOf("Sweet Potato", "Flour", "Egg"), listOf("Bake potatoes", "Mix dough", "Boil gnocchi")),
    Recipe(3, "Spaghetti Bolognese", listOf("Spaghetti", "Ground Beef", "Tomato Sauce"), listOf("Make the sauce", "Cook spaghetti", "Combine and serve"))
)

@Composable
fun App(recipeViewModel: RecipeViewModel = viewModel()) {
    //rememberNavController - creates and remembers the NavController across recompositions
    //keeps track of back stack of composable that make app screens
    val navController = rememberNavController()

    //log the back stack when it changes, automatically updates when back stack changes
//    LaunchedEffect(navController) {
//        navController.currentBackStack.collect { backStackEntries ->
//            val routeList = backStackEntries.joinToString(separator = " -> ") { it.destination.route ?: "null" }
//            Log.d(TAG, "Current Back Stack: $routeList")
//        }
//    }

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            NavigationBar {
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when re-selecting the same item
                                launchSingleTop = true
                                // Restore state when re-selecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        //This is where navigation happens, NavHost is a composable that displays other composable
        //based on the current destination based on a path , acts as nav graph for your app
        NavHost(
            navController = navController, // controller manages navigation
            startDestination = Screen.Home.route, // the route of the first screen to be displayed
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    recipes = recipeViewModel.recipes,
                    onRecipeClick = { recipeId ->
                        navController.navigate("details/$recipeId")
                    }
                )
            }
            composable(Screen.AddRecipe.route) {
                AddRecipeScreen(
                    onAddRecipe = { title, ingredients, steps ->
                        recipeViewModel.addRecipe(title, ingredients, steps)
                        // Navigate back to home after adding, clearing the add screen from the stack
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen() // Placeholder for settings
            }
            composable("details/{recipeId}") { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull()
                val recipe = recipeViewModel.recipes.find { it.id == recipeId }
                if (recipe != null) {
                    DetailsScreen(navController = navController, recipe = recipe)
                } else {
                    Text("Recipe not found")
                }
            }
        }
    }
}

/*
HomeScreen()- this will represent a composable showing the Home Screen
argument: navController, the controller used to navigate other screens
 */
@Composable
fun HomeScreen(recipes: List<Recipe>, onRecipeClick: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Home Screen", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(recipes) { recipe ->
                RecipeItem(
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe.id) }
                )
            }
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
    ) {
        Text(
            text = recipe.title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

/*
DetailsScreen()- this will represent a composable showing the Details Screen
argument: navController, the controller used to navigate other screens
 */
@Composable
fun DetailsScreen(navController: NavController, recipe: Recipe) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
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
        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Go Back")
        }
    }
}

@Composable
fun AddRecipeScreen(onAddRecipe: (String, String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var steps by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Add a New Recipe", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Recipe Title") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = ingredients, onValueChange = { ingredients = it }, label = { Text("Ingredients (comma separated)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = steps, onValueChange = { steps = it }, label = { Text("Steps (comma separated)") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onAddRecipe(title, ingredients, steps) },
            enabled = title.isNotBlank()
        ) {
            Text("Add Recipe")
        }
    }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Text("Settings will be implemented here.", style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Whats_For_DinnerTheme {
        HomeScreen(recipes = initialRecipes, onRecipeClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    Whats_For_DinnerTheme {
        DetailsScreen(navController = rememberNavController(), recipe = initialRecipes.first())
    }
}

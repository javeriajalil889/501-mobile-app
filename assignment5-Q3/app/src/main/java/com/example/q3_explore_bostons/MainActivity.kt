package com.example.q3_explore_bostons
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.rememberNavController
import com.example.q3_explore_bostons.ui.theme.Q3_Explore_BostonsTheme
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Q3_Explore_BostonsTheme {
                val navController = rememberNavController()
                // get  current back stack entry
                val backStackEntry by navController.currentBackStackEntryAsState()
                // get  current route to determine the title and if we can go back
                val currentRoute = backStackEntry?.destination?.route ?: Screen.Home.route
                // determine if the back button should be shown
                val canNavigateBack = navController.previousBackStackEntry != null && currentRoute != Screen.Home.route

                //shows the respective titles in topappbar
                val currentTitle = when {
                    currentRoute == Screen.Home.route -> "Home"
                    currentRoute == Screen.Categories.route -> "Categories"
                    currentRoute.startsWith("list/") -> {
                        // for list screen, show the category name
                        val category = backStackEntry?.arguments?.getString("category")
                        "Category: ${category.orEmpty()}"
                    }
                    currentRoute.startsWith("details/") -> {
                        // for detail screen, show the place name
                        val placeId = backStackEntry?.arguments?.getInt("placeId")
                        val placeName = placeId?.let { DataSource.getPlaceById(it)?.name }
                        placeName.orEmpty()
                    }
                    else -> "Explore Boston"
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    // 5. Add the TopAppBar to the Scaffold
                    topBar = {
                        ExploreTopAppBar(
                            title = currentTitle,
                            canNavigateBack = canNavigateBack,
                            navigateUp = { navController.navigateUp() } //action for back button, navigateUp, takes us back to the top of stack
                        )
                    }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Categories : Screen("categories", "Categories", Icons.Default.Home)
    data object List : Screen("list", "List", Icons.Default.Home)
    data object Details : Screen("details", "Details", Icons.Default.Home)
}
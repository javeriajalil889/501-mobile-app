package com.example.assignment5_q2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Notes
import androidx.compose.material.icons.outlined.Task

import com.example.assignment5_q2.ui.theme.Assignment5_Q2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment5_Q2Theme {
                MainScreen()
            }
        }
    }
}
/**
 * creating a sealed class to define the screeens in our app. this ensures type safe navigation and for keeping
 * all screen related information (route,title,icon) in one place
 */
sealed class Screen(val route: String, val title: String, val icon: ImageVector){
    data object Notes: Screen("notes","Notes", Icons.Outlined.Notes)
    data object Task: Screen("task","Task", Icons.Outlined.Task)
    data object Calendar: Screen("calendar", "Calendar", Icons.Outlined.CalendarMonth)
}

//list of all our screens to easily iterate over for nav bar
val screens= listOf(
    Screen.Notes,
    Screen.Task,
    Screen.Calendar
)

/*
This is main compposable that sets up UI -> Scaffold, navhost, and bottom nav bar
 */
@Composable
fun MainScreen(){
    //rememberNavController, important remembers state even after recomposition
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar{
                //gets current back stack entry, tells us which screen is dispplayed
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination= navBackStackEntry?.destination
                
                
                screens.forEach{screen ->
                    NavigationBarItem(
                        label= {Text(screen.title)},
                        icon = { Icon(screen.icon, contentDescription = screen.title) },

                        // Determine if this item is currently selected.
                        // We check if the current route is part of the destination's hierarchy.
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        
                        //define the click action for the item
                        onClick={
                            navController.navigate(screen.route){
                                //pop up to the start destination of the graph to avoid
                                //building up a large stack of destinations on the back stack as users select items 
                                popUpTo(navController.graph.findStartDestination().id){
                                        saveState=true
                                }
                                    launchSingleTop=true
                                    restoreState=true
                                }
                            }
                            )
                        }
                }
        }

){ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Notes.route, //first screen to show
            modifier = Modifier.padding(innerPadding)
        ) {
            //define composables for each screen in our nav graph
            
            composable(Screen.Notes.route) { GenericScreen(screen = Screen.Notes) }
            composable(Screen.Task.route) { GenericScreen(screen = Screen.Task) }
            composable(Screen.Calendar.route) { GenericScreen(screen = Screen.Calendar) }
        }
    }
}

/*
A generic, reusable screen composable to avoid repetitive code. Displays
the title of the screen passed to it. 
@param screen, the Screen object containging the title to displau
 */
@Composable
fun GenericScreen(screen:Screen){
    Box(
        modifier=Modifier.fillMaxSize(), 
        contentAlignment= Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Icon(imageVector = screen.icon, contentDescription = null, modifier = Modifier.padding(bottom = 8.dp))
            Text(text = screen.title, style = MaterialTheme.typography.headlineMedium)        
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Assignment5_Q2Theme {
        MainScreen()
    }
}
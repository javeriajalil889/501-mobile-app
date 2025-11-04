package com.example.q3_explore_bostons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import com.example.q3_explore_bostons.DataSource.categories
import com.example.q3_explore_bostons.DataSource.places
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp



//navController, is passed as a parameter, in each screen to handle navigation

//Home Screen
@Composable
fun HomeScreen(navController: NavController, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Welcome to Explore Boston")
        Button(onClick={navController.navigate(Screen.Categories.route)}){
            Text("Start exploring ")
        }
    }
}

// Categories Screen
@Composable
fun CategoriesScreen(navController: NavController, modifier:Modifier = Modifier) {
    // We use `items` to iterate through the list of categories from our DataSource
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(categories) { category -> // Iterate over the categories list
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 4.dp)
                    .clickable {
                        navController.navigate("list/$category")
                    }
            ) {
                Text(text = category, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

//List Screen
@Composable
fun ListScreen(navController: NavController, category: String?, modifier: Modifier = Modifier) {
    // Filter the places from the DataSource to get only the ones in the selected category
    val itemsToShow = places.filter { it.category == category }
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(itemsToShow) { place ->
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 4.dp)
                    .clickable {
                        // Navigate to the details screen, passing the ID of the selected place
                        navController.navigate("details/${place.id}")
                    }
            ) {
                Text(text = place.name, modifier = Modifier.padding(16.dp))
            }
        }
    }
}
//Details Screen
@Composable
//  add navController as a param
fun DetailScreen(navController: NavController, placeId: Int?, modifier: Modifier = Modifier) {
    val place = placeId?.let { DataSource.getPlaceById(it) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (place != null) {
            Text(text = "Name: ${place.name}")
            Text(text = "Description: ${place.description}")
            //add button to navigate home
            Button(
                onClick = {
                    navController.navigate(Screen.Home.route) {
                        //key part for stack management, takes us back to home
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text(text = "Go to Home")
            }
        } else {
            Text(text = "Place not found.")
        }
    }
}

//Composable for reusable top app bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreTopAppBar(title: String, canNavigateBack: Boolean, navigateUp: () -> Unit,
                     modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(title) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

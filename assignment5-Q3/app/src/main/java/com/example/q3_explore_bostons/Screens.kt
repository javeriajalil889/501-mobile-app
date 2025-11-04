package com.example.q3_explore_bostons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import com.example.q3_explore_bostons.DataSource.DataSource.categories
import com.example.q3_explore_bostons.DataSource.DataSource.getPlaceById
import com.example.q3_explore_bostons.DataSource.DataSource.places

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
fun DetailScreen(placeId: Int?, modifier: Modifier = Modifier) {
    // find the specific place using the ID passed as an argument
    val place = placeId?.let { getPlaceById(it) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (place != null) {
            Text(text = "Name: ${place.name}")
            Text(text = "Description: ${place.description}")
        } else {
            Text(text = "Place not found.")
        }
    }
}
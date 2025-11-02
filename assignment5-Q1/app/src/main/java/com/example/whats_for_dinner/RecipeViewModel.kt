package com.example.whats_for_dinner

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class RecipeViewModel : ViewModel() {
    // Hold the list of recipes as mutable state within the ViewModel
    var recipes by mutableStateOf(initialRecipes)
        private set // Only the ViewModel can change the list

    fun addRecipe(title: String, ingredients: String, steps: String) {
        val newId = (recipes.maxOfOrNull { it.id } ?: 0) + 1
        val newRecipe = Recipe(
            id = newId,
            title = title,
            ingredients = ingredients.split(",").map { it.trim() },
            steps = steps.split(",").map { it.trim() }
        )
        recipes = recipes + newRecipe
    }
}

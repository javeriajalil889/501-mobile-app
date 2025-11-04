package com.example.q3_explore_bostons

class DataSource {
    //represents a single place of interest
    data class Place(
        val id: Int,
        val category: String,
        val name: String,
        val description: String,
    )
    object DataSource{
        val places = listOf(
            Place(1, "Museums", "Museum of Fine Arts", "Home to a vast collection of art"),
            Place(2, "Museums", "The Metropolitan Museum of Art", "Features exhibits around the world: Africa, Asia, North America, Europe, Oceania, and the Americas, and art"),
            Place(3, "Park", "Boston Commons", "A central public park in downtown Boston"),
            Place(4, "Restaurant", "Fire and Ice", "A unique buffet style experience, including hibachi style"),
            Place(5, "Restaurants", "Union Oyster House", "One of America's oldest restaurants."),
            )

       //helper function to get the list of unique categories from place, sort of like dictionary
        val categories = places.map { it.category }.distinct()

        //helper function to find specific place based on it ID
        fun getPlaceById(id:Int): Place?{
            return places.find { it.id == id }

        }

    }
}
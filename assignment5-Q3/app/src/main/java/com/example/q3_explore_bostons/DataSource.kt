package com.example.q3_explore_bostons

// data class to represent a place of interest
data class Place(
    val id: Int,
    val name: String,
    val description: String,
    val category: String
)

// Singleton object to act as a data source for the app
object DataSource {
    // list of categories based on the places below
    val categories = listOf(
        "Museums",
        "Parks",
        "Restaurants"
    ).distinct() // Using distinct() to avoid duplicates

    // list of places in Boston
    val places = listOf(
        Place(1, "Museum of Fine Arts", "Home to a vast collection of art", "Museums"),
        Place(2, "The Metropolitan Museum of Art", "Features exhibits around the world...", "Museums"),
        Place(3, "Boston Commons", "A central public park in downtown Boston.", "Parks"),
        Place(4, "Fire and Ice", "A unique buffet style experience...", "Restaurants"),
        Place(5, "Union Oyster House", "One of America's oldest restaurants.", "Restaurants")
    )

    /**
     * returns a specific place from the list based on its ID.
     */
    fun getPlaceById(id: Int): Place? {
        return places.find { it.id == id }
    }
}

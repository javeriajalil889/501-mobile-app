Q1 What's For Dinner:

Does the follwowing:

• Home screen: list of recipe names (use LazyColumn)
• Detail screen: displays full recipe (title, ingredients, steps) using data passed via arguments
• Add Recipe screen: form for entering new recipe (basic state management)
• Use NavHostController, NavHost, and define a sealed Routes class
• Use navigate(route + "/{id}") to pass an argument and read it via backStackEntry
• Use popUpTo() to control stack behavior when adding a new recipe
• Prevent multiple copies of the same screen using launchSingleTop
• Style and layout using Scaffold and consistent navigation
• Implement a BottomNavigation bar for “Home”, “Add”, and “Settings”; Persist recipes using in-memory state in ViewModel


USE OF AI:
Primary Use: I used AI to help debug dependency and Gradle build issues. 
When I encountered errors like Unresolved reference for dependencies or resource 
linking failures (<adaptive-icon> requires SDK 26), I provided the error logs to the AI
to get help. This was helpful for correcting my build.gradle.kts and libs.versions.toml files. 
AI was a useful tool for specific instanaces but you need to make careful oversight, b/c sometimes it worsens the issue. 


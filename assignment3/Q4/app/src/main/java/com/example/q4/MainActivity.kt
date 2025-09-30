package com.example.q4

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.q4.ui.theme.Q4Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAG = "ScaffoldWithBarsDemo"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Q4Theme {
                MainScreenWithScaffoldFeatures()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithScaffoldFeatures() {
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope: CoroutineScope = rememberCoroutineScope()
    var showSearchView by remember { mutableStateOf(false) }
    var selectedBottomNavItem by remember { mutableIntStateOf(0) } // 0 for Home, 1 for Settings, 2 for Profile
    val snackbarHostState = remember { SnackbarHostState() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                onItemSelected = { selectedItem ->
                    Log.d(TAG, "$selectedItem selected from drawer's callback in MainScreen")
                    scope.launch {
                        drawerState.close()
                    }
                    // TODO: Implement actual navigation or action based on the `selectedItem`.
                },
                onCloseDrawer = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        },
        gesturesEnabled = drawerState.isOpen || !showSearchView
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                AppTopBar(
                    title = "Scaffold with TopAppBar, BottomBar, and FAB", // app title
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    },
                    onSearchClick = {
                        showSearchView = !showSearchView
                    }
                )
            },
            bottomBar = {
                AppBottomNavigationBar(
                    selectedItemIndex = selectedBottomNavItem,
                    onItemSelected = { index ->
                        selectedBottomNavItem = index
                        val itemName = when (index) {
                            0 -> "Home"
                            1 -> "Settings"
                            else -> "Profile"
                        }
                        Log.d(TAG, "$itemName clicked from Bottom Navigation!")
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("FAB Clicked!")
                    }
                    Log.d(TAG, "FAB Clicked!")
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Item")
                }
            },
            content = { innerPadding ->
                Log.d(TAG, "Scaffold content lambda called with PaddingValues: $innerPadding")
                Box(modifier = Modifier.fillMaxSize()) {
                    MailListContent(
                        modifier = Modifier
                            .padding(innerPadding) // apply padding from Scaffold.
                            .fillMaxSize()
                    )
                    if (showSearchView) {
                        SearchViewOverlay(
                            onCloseSearch = {
                                showSearchView = false
                                Log.d(TAG, "Search view overlay closed.")
                            }
                        )
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String, onMenuClick: () -> Unit, onSearchClick: () -> Unit) {
    TopAppBar(
        title = { Text(title, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Open Menu"
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
fun AppBottomNavigationBar(selectedItemIndex: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedItemIndex == 0,
            onClick = { onItemSelected(0) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = selectedItemIndex == 1,
            onClick = { onItemSelected(1) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selectedItemIndex == 2,
            onClick = { onItemSelected(2) }
        )
    }
}


@Composable
fun AppDrawerContent(
    onItemSelected: (String) -> Unit,
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(modifier = modifier.fillMaxWidth(0.8f)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("App Menu", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onCloseDrawer) {
                    Icon(Icons.Filled.Close, contentDescription = "Close Drawer")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            val drawerItems = listOf(
                "Inbox" to Icons.Filled.Email,
                "Favorites" to Icons.Filled.Favorite,
                "Settings" to Icons.Filled.Settings,
                "About" to Icons.Filled.Info
            )

            drawerItems.forEach { (label, icon) ->
                NavigationDrawerItem(
                    icon = { Icon(icon, contentDescription = label) },
                    label = { Text(label) },
                    selected = false, // You might want to manage selected state here too
                    onClick = {
                        Log.d(TAG, "Drawer item clicked: $label")
                        onItemSelected(label)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    }
}

@Composable
fun MailListContent(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(40) { index ->
            MessageItem(itemIndex = index)
        }
    }
}

@Composable
fun MessageItem(itemIndex: Int, modifier: Modifier = Modifier) {
    Text(
        text = "Message # $itemIndex",
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
            .padding(16.dp)
    )
}

@Composable
fun SearchViewOverlay(onCloseSearch: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.8f)) // Semi-transparent background
            .clickable(onClick = onCloseSearch) // Close when clicking outside (optional)
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            Text("Search View", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            // Add your search input field and results list here
            Button(onClick = onCloseSearch) {
                Text("Close Search")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Q4Theme {
        MainScreenWithScaffoldFeatures()
    }
}

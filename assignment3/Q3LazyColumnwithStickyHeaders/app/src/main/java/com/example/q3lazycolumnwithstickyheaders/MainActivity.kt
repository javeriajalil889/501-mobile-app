package com.example.q3lazycolumnwithstickyheaders // Use your actual package name

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.q3lazycolumnwithstickyheaders.ui.theme.Q3LazyColumnWithStickyHeadersTheme // Use your actual theme
import kotlinx.coroutines.launch

data class Contact(val name: String, val phoneNumber: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Q3LazyColumnWithStickyHeadersTheme {
                ContactListScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactListScreen(modifier: Modifier = Modifier) {
    //50 contact numberer
    val contacts = remember {
        listOf(
            Contact("Alice", "123-456-7890"),
            Contact("Bob", "987-654-3210"),
            Contact("Charlie", "555-123-4567"),
            Contact("Diana", "444-987-6543"),
            Contact("Ethan", "333-222-1111"),
            Contact("Fiona", "222-333-4444"),
            Contact("George", "111-888-9999"),
            Contact("Hannah", "666-777-8888"),
            Contact("Ian", "777-111-2222"),
            Contact("Jasmine", "888-444-5555"),
            Contact("Kevin", "999-000-1111"),
            Contact("Laura", "123-111-2222"),
            Contact("Michael", "234-567-8901"),
            Contact("Nina", "345-678-9012"),
            Contact("Oliver", "456-789-0123"),
            Contact("Paula", "567-890-1234"),
            Contact("Quinn", "678-901-2345"),
            Contact("Rachel", "789-012-3456"),
            Contact("Sam", "890-123-4567"),
            Contact("Tina", "901-234-5678"),
            Contact("Uma", "321-654-9870"),
            Contact("Victor", "210-987-6543"),
            Contact("Wendy", "432-109-8765"),
            Contact("Xander", "543-210-9876"),
            Contact("Yara", "654-321-0987"),
            Contact("Zane", "765-432-1098"),
            Contact("Amber", "876-543-2109"),
            Contact("Brian", "987-654-3211"),
            Contact("Clara", "135-246-3579"),
            Contact("David", "246-357-4680"),
            Contact("Elena", "357-468-5791"),
            Contact("Frank", "468-579-6802"),
            Contact("Grace", "579-680-7913"),
            Contact("Henry", "680-791-8024"),
            Contact("Isla", "791-802-9135"),
            Contact("Jack", "802-913-0246"),
            Contact("Kylie", "913-024-1357"),
            Contact("Liam", "024-135-2468"),
            Contact("Maya", "135-246-3570"),
            Contact("Noah", "246-357-4681"),
            Contact("Olivia", "357-468-5792"),
            Contact("Peter", "468-579-6803"),
            Contact("Queen", "579-680-7914"),
            Contact("Riley", "680-791-8025"),
            Contact("Sophia", "791-802-9136"),
            Contact("Tyler", "802-913-0247"),
            Contact("Ursula", "913-024-1358"),
            Contact("Violet", "024-135-2469"),
            Contact("William", "135-246-3571"),
            Contact("Zoey", "246-357-4682")
        )
    }

    // grouped and sorted contacts
    val groupedContacts = remember(contacts) {
        contacts.groupBy { it.name.first().uppercaseChar() }.toSortedMap()
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // derived state for FAB visibility: show if scrolled past item 10
    val showFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 10
        }
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(index = 0)
                    }
                }) {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Scroll to top")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize() //  LazyColumn takes up available space within Scaffold
        ) {
            groupedContacts.forEach { (letter, contactsInGroup) ->
                stickyHeader {
                    Text(
                        text = " $letter",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
                items(contactsInGroup) { contact ->
                    ContactItem(contact = contact)
                }
            }
        }
    }
}

@Composable
fun ContactItem(contact: Contact, modifier: Modifier = Modifier) {
    Text(
        text = "${contact.name} - ${contact.phoneNumber}",
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun ContactListPreview() {
    Q3LazyColumnWithStickyHeadersTheme {
        ContactListScreen()
    }
}

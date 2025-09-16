package com.example.assignment2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.assignment2.ui.theme.Assignment2Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember





class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KotlinPracticeScreen(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun ColorCard(color: Color, label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .background(color),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = label,
            color = Color.White
        )
    }

}

@Composable
fun ColorCardScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ColorCard(color = Color.LightGray, label = "Grey", modifier=Modifier
            .size(width = 200.dp, height = 200.dp)
            .border(
                border = BorderStroke(2.dp, Color.Black)
            )
        )

        ColorCard(color = Color.DarkGray, label = "Dark Grey", modifier=Modifier
            .size(width = 250.dp, height = 250.dp)
            .border(
                border = BorderStroke(2.dp, Color.Blue)
            )
        )

        ColorCard(color = Color.LightGray, label = "Light Grey", modifier=Modifier
            .size(width = 300.dp, height = 300.dp)
            .border(
                border = BorderStroke(2.dp, Color.Green)
            )
        )
    }
}
//
@Preview(showBackground = true)
@Composable
fun ColorCardScreenPreview() {
    Assignment2Theme {
        ColorCardScreen()
    }
}

@Composable
fun ToggleCard(
    initialMessage: String,
    toggledMessage: String,
    cardBackgroundColor: Color = Color.White,
    modifier: Modifier = Modifier
) {
    var isToggled by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { isToggled = !isToggled },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isToggled) toggledMessage else initialMessage,
                color = Color.Black
            )
        }
    }
}

@Composable
fun MyToggleScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ToggleCard(
            initialMessage = "Tap to see a fun fact about Boston!",
            toggledMessage = "The oldest public park in the USA is in Boston.\n" +
                    "Boston Common was established in 1634 and is still a popular place for folks to gather.",
            cardBackgroundColor = Color.Cyan
        )

        ToggleCard(
            initialMessage = "Click for another Boston fact!",
            toggledMessage = "The very first chocolate factory in the USA was in Boston.\n" +
                    "The year was 1765 when Walter Baker opened his chocolate factory in Dorchester.",
            cardBackgroundColor = Color.Cyan
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyToggleScreenPreview() {
    Assignment2Theme {
        MyToggleScreen()
    }
}


@Composable
fun KotlinPracticeScreen(modifier:Modifier = Modifier){
    var inputText by remember { mutableStateOf("cat") }
    val nullableString: String? = "This is a nullable string"
    var counter by rememberSaveable { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text (text= "When expression result: ")
        Text(
            text = when (inputText){
                "cat" -> "meow"
                "cow" -> "moo"
                "panda" -> "blah blah"
                "bird" -> "tweet"
                "fish" -> "gulp"
                else -> "I don't know that animal"


            }
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { inputText = "cat" }) {
                Text("Cat")
            }

            Button(onClick = { inputText = "fish" }) {
                Text("Fish")
            }

            Button(onClick ={inputText = "panda"}) {
                Text("Panda")
            }
            Button(onClick = { inputText = "other" }) {
                Text("Other")
            }
        }

        nullableString?.let {
            Text(text = "Nullable string message:")
            Text(text = it)
        }

        // Counter
        Text(text = "Counter: $counter")
        Button(
            onClick = {
                if (counter < 5) {
                    counter++
                }
            },
            enabled = counter < 5
        ) {
            Text(text = "Increment Counter (Max 5)")
        }
        if (counter >=5) {
            Text("Counter cannot go above 5.")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KotlinPracticeScreenPreview() {
    Assignment2Theme {
        KotlinPracticeScreen()
    }
}

package com.example.q1lifetracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.q1lifetracker.ui.theme.Q1LifeTrackerTheme

class MainActivity : ComponentActivity() {
    private val TAG = "ActivityStateTransition"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "[Activity] ==> onCreate: The Activity is being created.")
        enableEdgeToEdge()
            // setContent: entry point for jetPack Compose
        // defines UI for this activity
        setContent {
            Q1LifeTrackerTheme {
                    // A surface is basic building block in Material Design
                Surface(
                    modifier = Modifier.fillMaxSize(), // Make the surface fill the entire screen.
                    color = MaterialTheme.colorScheme.background
                ) {
                    //call our main composable function which contains the UI, and its own lifecycle observer
                }
               Greeting("Android")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Q1LifeTrackerTheme {
        Greeting("Android")
    }
}
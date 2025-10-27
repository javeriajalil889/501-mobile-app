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

/**
 * onStart is called when the activity is becoming visible to the user. State Transition
 * created -> started
 */

override fun onStart(){
    super.onStart()
    Log.d(TAG, "[Activity] ==> onStart: The Activity is becoming visible to the user.")
}

/**
 * onResume is called when the activity will start interacting with the user.
 * State Transition : Started --> Resumed (App is running and interactive)
 */

override fun onResume(){
    Log.d(TAG, "[Activity] ==> onResume: The Activity is interactive")
}

/**
 * onPause is called when the activity is no longer visible to the user. running in background
 * State Transition : Resumed --> Paused (App is running but not interactive)
 */

override fun onPause(){
    super.onPause()
    Log.d(TAG, "[Activity] ==> onPause: The Activity is going into the background.")
}


/**
 * onStop is called when the activity is no longer needed to be visible to the user.
 * State Transition : Paused --> Stopped (App is running but not interactive)
 */

override fun onStop(){
    Log.d(TAG, "[Activity] ==> onStop: The Activity is no longer visible to the user."))
}


/**
 * onDestroy is the final call you recieve before your activity is destroyed
 * This happens when the user presses Back or the system needs to reclaim memory.
 * State Transition: Stopped -> Destroyed
 */
override fun onDestroy(){
    super.onDestroy()
    Log.d(TAG, "[Activity] ==> onDestroy: The Activity is being destroyed.")
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
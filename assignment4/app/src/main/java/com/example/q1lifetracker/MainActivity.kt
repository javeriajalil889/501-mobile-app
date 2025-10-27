package com.example.q1lifetracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.q1lifetracker.ui.theme.Q1LifeTrackerTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

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
              LifecycleDemoScreen()
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
        Log.d(TAG, "[Activity] ==> onStop: The Activity is no longer visible to the user.")
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
}





@Composable
fun LifecycleDemoScreen(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current) {
    // A constant for logging from within this Composable.
    val TAG = "ActivityStateTransition"

    // `DisposableEffect` is a side-effect Composable used for managing resources
    // that need to be cleaned up when the composable leaves the screen (is "disposed").
    // It's perfect for adding and removing observers.
    // The `key1 = lifecycleOwner` means this effect will re-run if the lifecycleOwner changes.
    DisposableEffect(lifecycleOwner) {
        // Create an observer that logs lifecycle events.
        val observer = LifecycleEventObserver { _, event ->
            // We can log the event that the Composable's observer receives.
            // This shows how a Composable can react to the Activity's state.
            Log.d(TAG, "[Composable] Observed Event: ${event.name}")
        }

        // Add the observer to the lifecycle of the owner (our Activity).
        lifecycleOwner.lifecycle.addObserver(observer)

        // The `onDispose` block is crucial. It's called when the Composable
        // is removed from the composition. We must clean up our observer here
        // to prevent memory leaks.
        onDispose {
            Log.d(TAG, "[Composable] Disposing Effect. Removing observer.")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // This is the UI layout for our screen.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center, // Center content vertically.
        horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally.
    ) {
        Text(
            text = "Jetpack Compose Lifecycle Demo",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Open Logcat with the tag 'ActivityStateTransition' to see the lifecycle events.",
            textAlign = TextAlign.Center, // Center the text content.
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Q1LifeTrackerTheme {
        LifecycleDemoScreen()
    }
}
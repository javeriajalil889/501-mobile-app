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
import androidx.activity.viewModels
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class MainActivity : ComponentActivity() {
    //get reference to the mainviewmodel
    //here we get ViewModel instance in an activity, viewModels(), handles creating
    //the viewModel the first time, and retriving the exisiting instance on subsequent
    //creations of an acitivity (such as a rotation)
    private val viewModel by viewModels<MainViewModel>()
    private val TAG = "ActivityStateTransition"
    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * `onCreate` is the very first method called when the activity is created.
         * This is where you set up the activity, including setting the Compose content.
         * State Transition: (Does not exist) -> Created
         */
        super.onCreate(savedInstanceState)
        //log the onCreate  event
        //addLog(), call this function, in our viewModel, passing name of lifecyle event
        //every time an event occurs, it will be added inside our viewModel object.
        viewModel.addLog("onCreate")
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
                    LogListScreen(viewModel=viewModel)
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
        //log onStart event
        viewModel.addLog("onStart")
        Log.d(TAG, "[Activity] ==> onStart: The Activity is about to become visible.")

    }

    /**
     * onResume is called when the activity will start interacting with the user.
     * State Transition : Started --> Resumed (App is running and interactive)
     */

    override fun onResume(){
        super.onResume()
        viewModel.addLog("onResume")
        Log.d(TAG, "[Activity] ==> onResume: The Activity is interactive")
    }

    /**
     * onPause is called when the activity is no longer visible to the user. running in background
     * State Transition : Resumed --> Paused (App is running but not interactive)
     */

    override fun onPause(){
        super.onPause()
        viewModel.addLog("onPause")
        Log.d(TAG, "[Activity] ==> onPause: The Activity is going into the background.")
    }


    /**
     * onStop is called when the activity is no longer needed to be visible to the user.
     * State Transition : Paused --> Stopped (App is running but not interactive)
     */

    override fun onStop(){
        super.onStop()
        viewModel.addLog("onStop")
        Log.d(TAG, "[Activity] ==> onStop: The Activity is no longer visible to the user.")
    }


    /**
     * onDestroy is the final call you recieve before your activity is destroyed
     * This happens when the user presses Back or the system needs to reclaim memory.
     * State Transition: Stopped -> Destroyed
     */
    override fun onDestroy(){
        super.onDestroy()
        viewModel.addLog("onDestroy")
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



@Composable
fun LogListScreen(viewModel: MainViewModel){
    //observe the logs stateFlow from viewModel
    //collectAsState(), convert the flow into State, which recomposes the UI on updates
    val logs by viewModel.logs.collectAsState()
    //LazyColumn efficent way to display scorelling list
    //only composes and lays out items that are currently visible
    LazyColumn(modifier=Modifier.padding(16.dp)){
        items(logs) { log -> // Loop through each log in the list
            Text(
                text = "[${log.timestamp}] Event: ${log.eventName}",
                color=log.color,
                modifier= Modifier.padding(vertical=4.dp)
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Q1LifeTrackerTheme {
        LifecycleDemoScreen()
    }
}
package com.example.q2counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.q2counter.ui.theme.Q2CounterTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Q2CounterTheme {
                Surface(
                    modifier=Modifier.fillMaxSize(),
                    color=MaterialTheme.colorScheme.background
                ){
                    Q2CounterScreen()
                }
            }
        }
    }
}

@Composable
fun Q2CounterScreen(mainViewModel: MainViewModel = viewModel()){
    //managing snackbar display
    val snackbarHostState= remember { SnackbarHostState() }
    //coroutine scope tied to composoble lifecycle
    var coroutineScope= rememberCoroutineScope()

    var simpleFlowValue by remember { mutableStateOf("Simple Flow: Waiting...") }
    var isCollectingSimpleFlow by remember { mutableStateOf(false) }

    val counterState by mainViewModel.counterStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = mainViewModel.eventSharedFlow) {
        println("Compose Log: LaunchedEffect for eventSharedFlow started.")
        mainViewModel.eventSharedFlow.collectLatest { eventMessage ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = eventMessage,
                    duration = SnackbarDuration.Short
                )
            }
            println("Compose Log: Collected from eventSharedFlow - '$eventMessage'")
        }
        println("Compose Log: LaunchedEffect for eventSharedFlow finished or cancelled.")
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // --- Simple Flow Section ---
            Text(text = simpleFlowValue, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp)) // Vertical space.
            Button(
                onClick = {
                    if (!isCollectingSimpleFlow) {
                        isCollectingSimpleFlow = true
                        println("Compose Log: 'Start Simple Flow' button clicked. isCollectingSimpleFlow set to true.")
                    }
                },
                enabled = !isCollectingSimpleFlow || simpleFlowValue.contains("Finished")
            ) {
                Text(
                    if (isCollectingSimpleFlow && !simpleFlowValue.contains("Finished")) "Collecting Simple Flow..."
                    else "Start Simple Flow Collection"
                )
            }


            if (isCollectingSimpleFlow) {
                LaunchedEffect(key1 = mainViewModel.simpleFlow) { // Using flow instance as key ensures if it changes, collection restarts.
                    println("Compose Log: LaunchedEffect for simpleFlow collection started.")
                    simpleFlowValue = "Simple Flow: Collecting..."
                    try {
                        // Collect values from the simpleFlow.
                        mainViewModel.simpleFlow.collect { value ->
                            simpleFlowValue = "Simple Flow: $value" // Update UI with the new value.
                            println("Compose Log: Collected from simpleFlow - $value")
                        }
                        simpleFlowValue = "Simple Flow: Finished!"
                        println("Compose Log: simpleFlow collection completed normally.")
                    } finally {
                        println("Compose Log: simpleFlow collection ended (completed or cancelled).")
                        isCollectingSimpleFlow = false // Reset the button state, allowing collection again.
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- StateFlow Section ---
            Text(text = "StateFlow Counter: $counterState", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                mainViewModel.incrementCounter() // Call ViewModel function to update StateFlow.
                println("Compose Log: 'Increment Counter' button clicked.")
            }) {
                Text("Increment Counter (StateFlow)")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- SharedFlow Section ---
            Button(onClick = {
                mainViewModel.sendEvent("Event from Lecture4_Example2!") // Call ViewModel to send a SharedFlow event.
                println("Compose Log: 'Send SharedFlow Event' button clicked.")
            }) {
                Text("Send SharedFlow Event (Snackbar)")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Q2CounterTheme {
        Q2CounterScreen()
    }
}
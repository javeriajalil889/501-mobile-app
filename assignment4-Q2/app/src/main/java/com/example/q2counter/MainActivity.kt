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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.q2counter.ui.theme.Q2COUNTERTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Q2COUNTERTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "counter",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("counter") {
                            CounterScreen(navController = navController, modifier = Modifier.padding(innerPadding))
                        }
                        composable("settings") {
                            SettingsScreen(navController = navController)
                        }

                    }
                }
            }
        }
    }

}

@Composable
fun CounterScreen(mainViewModel: MainViewModel=viewModel(), modifier: Modifier, navController: NavController){
    val counter by mainViewModel.counterStateFlow.collectAsStateWithLifecycle()
    val isAutoMode by mainViewModel.isAutoMode.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        //display counter
        Text(text="Counter:$counter", style=MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        //Increment button
        Button(onClick={mainViewModel.incrementCounter()}){
            Text(text="Increment")

        }
        //Decrement Button
        Button(onClick={mainViewModel.decrementCounter()}){
            Text(text="Decrement")
        }
        //Reset Button
        Button(onClick={mainViewModel.resetCounter()}){
            Text(text="Reset")
        }

        Button(onClick={mainViewModel.toggleAutoMode()}){
          Text(if(isAutoMode) "Auto Mode: ON" else "Auto Mode: OFF")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick={navController.navigate("settings")}){
            Text(text="Settings")
        }

    }
}

@Composable
fun SettingsScreen(
    navController: NavController,
    mainViewModel: MainViewModel = viewModel()
) {
    val interval by mainViewModel.autoInterval.collectAsStateWithLifecycle()
    var newValue by remember { mutableStateOf(interval.toString()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Auto-Increment Interval (ms)", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Text field to edit interval
        OutlinedTextField(
            value = newValue,
            onValueChange = { newValue = it },
            label = { Text("Interval in milliseconds") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save button
        Button(onClick = {
            val longValue = newValue.toLongOrNull()
            if (longValue != null && longValue > 0) {
                mainViewModel.setAutoInterval(longValue)
                navController.popBackStack()
            }
        }) {
            Text("Save")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}


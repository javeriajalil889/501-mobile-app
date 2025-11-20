package com.example.assignment4_q3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignment4_q3.ui.theme.Assignment4Q3Theme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assignment4Q3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TemperatureDashboard()
                }
            }
        }
    }
}


@Composable
fun TemperatureDashboard(viewModel: TemperatureViewModel = viewModel()) {
    // colelcts the state from the ViewModel in a lifecycle-aware manner
    val readings by viewModel.temperatureReadings.collectAsStateWithLifecycle()
    val isRunning by viewModel.isRunning.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Temperature Dashboard", style = MaterialTheme.typography.headlineMedium)

        // displays summary stats
        Text("Current: ${readings.lastOrNull()?.roundToInt() ?: "N/A"}°F")
        Text("Avg: ${viewModel.average?.roundToInt() ?: "N/A"}°F")
        Text("Min: ${viewModel.min?.roundToInt() ?: "N/A"}°F")
        Text("Max: ${viewModel.max?.roundToInt() ?: "N/A"}°F")

        // bar chart via canvas
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            val barWidth = size.width / (readings.size.coerceAtLeast(1))

            // draws a rectangle for each temperature reading
            readings.forEachIndexed { i, temp ->
                val height = (((temp - 65f) / 20f) * size.height).coerceAtLeast(0f)
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(i * barWidth, size.height - height),
                    size = Size(barWidth * 0.9f, height) // 90% width for spacing
                )
            }
        }

        // dispaly the list of temperature readings with their index
        readings.forEachIndexed { i, temp ->
            Text("$i → ${temp.roundToInt()}°F")
        }

        Spacer(Modifier.height(16.dp))

        // Show Pause or Resume button based on the simulation state
        if (isRunning) {
            Button(onClick = { viewModel.pause() }) {
                Text("Pause")
            }
        } else {
            Button(onClick = { viewModel.resume() }) {
                Text("Resume")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Assignment4Q3Theme {
        TemperatureDashboard()
    }
}

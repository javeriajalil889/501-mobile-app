package com.example.compass_and_digital_level

import android.content.Context
import android.hardware.Sensor
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
import com.example.compass_and_digital_level.ui.theme.Compass_and_digital_levelTheme

//set up sensors
val sensorManager= context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
val acclerrometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
val magentometer= sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Compass_and_digital_levelTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
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
    Compass_and_digital_levelTheme {
        Greeting("Android")
    }
}
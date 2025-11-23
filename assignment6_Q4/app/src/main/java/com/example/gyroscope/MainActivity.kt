package com.example.gyroscope

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.gyroscope.ui.theme.GyroscopeTheme



class MainActivity : ComponentActivity() {

    // instantiate the sensor manager
    private lateinit var gyroscopeSensor: GyroscopeSensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        gyroscopeSensor = GyroscopeSensor(this)
        gyroscopeSensor.start() // start listening for sensor data

        setContent {
            GyroscopeTheme {
                // collect the sensor data as state
                val rotation by gyroscopeSensor.rotation.collectAsState()

                // pass the rotation data to our GameScreen
                GameScreen(rotation = rotation)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gyroscopeSensor.stop() // stop listening when the app is destroyed
    }
}

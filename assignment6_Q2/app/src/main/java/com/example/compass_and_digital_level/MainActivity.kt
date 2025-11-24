package com.example.compass_and_digital_level

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    // sensors
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    private var gyroscope: Sensor? = null


    // viewmodel to store azimuth
    private val compassViewModel = CompassViewModel()

    //sensor readings
    private var gravity = FloatArray(3)
    private var geomagnetic = FloatArray(3)

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                when (it.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> gravity = it.values.clone()
                    Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic = it.values.clone()
                }

                val R = FloatArray(9)
                val I = FloatArray(9)
                if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(R, orientation)
                    val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                    compassViewModel.updateAzimuth(azimuth)
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // sensor intitalizaton
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        setContent {
            MaterialTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    CompassScreen(
                        viewModel = compassViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
       // this function on resume will register the sensor listener
        override fun onResume() {
        super.onResume()
        accelerometer?.let { sensorManager.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_UI) }
        magnetometer?.let { sensorManager.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_UI) }
    }
    // this function will unregister the sensor listener
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorEventListener)
    }
}

//this is the compass screen composable
@Composable
fun CompassScreen(viewModel: CompassViewModel, modifier: Modifier) {
    val azimuth by viewModel.azimuth

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010)),
        contentAlignment = Alignment.Center
    ) {
        // compass background circle
        Box(
            modifier = Modifier
                .size(300.dp)
                .background(Color.DarkGray, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // compass needle
            Image(
                painter = painterResource(id = R.drawable.compass_needle),
                contentDescription = "Compass Needle",
                modifier = Modifier
                    .size(200.dp)
                    .rotate(-azimuth) // rotate opposite of azimuth
            )
        }

        //displays heading
        Text(
            text = "${azimuth.toInt()}°",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}


package com.example.altitude_change

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.pow

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var pressureSensor: Sensor? = null
    private lateinit var altText: TextView
    private lateinit var mainLayout: ConstraintLayout

    //pressue
    private val P0 = 1013.25f
    // var to hold the current pressure
    private var currentPressure: Float = P0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initailize UI elements from the XML layout
        altText = findViewById(R.id.altText)
        mainLayout = findViewById(R.id.mainLayout)

        // getting the Android sensor service
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // getting the default pressure sensor on the device
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        // if the emulator or device has no pressure sensor, display a message
        if (pressureSensor == null) {
            altText.setText(R.string.sensor_not_available)
        }

        //tap the layout to decrease pressure (increase altitude) this doesn;t work entirely
        //instead I use pressue sensore in emulator to simulate the change in altitude
        mainLayout.setOnClickListener {
            // decrease pressure by 5
            currentPressure -= 5.0f
            // manually update the UI with the new simulated pressure
            updateAltitudeUI(currentPressure)
        }
    }

    override fun onResume() {
        super.onResume()
        // register the sensor listener
        pressureSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        // unregister the listener when the app is paused
        sensorManager.unregisterListener(this)
    }

    // this method is called whenever the sensor reports a new value
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_PRESSURE) {
                // Get the pressure reading in hPa from the event
                val pressure = it.values[0]
                // Update our simulation variable with the real data
                currentPressure = pressure
                // Update the screen with the new reading
                updateAltitudeUI(pressure)
            }
        }
    }

    // this method is required by the SensorEventListener
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // We don't need to do anything here for this app
    }

    /**
     * calculates altitude and updates the UI (TextView and background color).
     */
    private fun updateAltitudeUI(pressure: Float) {
        // calcualtes altitude using the barometric formula
        val altitude = calculateAltitude(pressure)
        // update the TextView using the formatted string from strings.xml
        altText.text = getString(R.string.altitude_format, altitude)

        // Change background color: higher altitude= darker color
        val colorScale = (altitude / 4000.0f).coerceIn(0.0f, 1.0f)
        val grayValue = 255 - (colorScale * 205).toInt() // 255 (white) down to 50 (dark gray)
        mainLayout.setBackgroundColor(android.graphics.Color.rgb(grayValue, grayValue, grayValue))
    }

    /**
     * calculate altitude  using the barometric formula.
     * h = 44330 * [1 - (P/P0)^(1/5.255)]
     */
    private fun calculateAltitude(pressure: Float): Float {
        return 44330.0f * (1.0f - (pressure / P0).pow(1.0f / 5.255f))
    }
}

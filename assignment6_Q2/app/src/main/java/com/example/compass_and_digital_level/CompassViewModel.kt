package com.example.compass_and_digital_level

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CompassViewModel : ViewModel() {
    // azimuth in degrees
    var azimuth = mutableStateOf(0f)
        private set

    fun updateAzimuth(angle: Float) {
        azimuth.value = angle
    }
}

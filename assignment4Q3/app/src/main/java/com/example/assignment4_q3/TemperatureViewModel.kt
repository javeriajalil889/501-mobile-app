package com.example.assignment4_q3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class TemperatureViewModel : ViewModel() {
    // Hold the last 20 temperature readings
    private val _temperatureReadings = MutableStateFlow<List<Float>>(emptyList())
    val temperatureReadings: StateFlow<List<Float>> = _temperatureReadings

    // Control whether the simulation is running
    private val _isRunning = MutableStateFlow(true)
    val isRunning: StateFlow<Boolean> = _isRunning

    /**
     * The init block is executed when the ViewModel instance is first created.
     */
    init {
        startTemperatureSimulation()
    }

    private fun startTemperatureSimulation() {
        viewModelScope.launch {
            while (true) {
                // Only generate new temperatures if the simulation is running
                if (_isRunning.value) {
                    // Generates a random temperature between 65 and 85
                    val newTemp = Random.nextFloat() * 20f + 65f
                    // Add the new temperature and keep only the last 20 readings
                    val updatedReadings = (_temperatureReadings.value + newTemp).takeLast(20)
                    _temperatureReadings.value = updatedReadings
                }
                // Wait for 2 seconds before the next update
                delay(2000)
            }
        }
    }

    fun pause() {
        _isRunning.value = false
    }

    fun resume() {
        _isRunning.value = true
    }

    // Summary calculations
    val average: Float?
        get() = temperatureReadings.value.takeIf { it.isNotEmpty() }?.average()?.toFloat()

    val min: Float?
        get() = temperatureReadings.value.minOrNull()

    val max: Float?
        get() = temperatureReadings.value.maxOrNull()
}

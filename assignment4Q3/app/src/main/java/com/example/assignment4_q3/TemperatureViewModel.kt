package com.example.assignment4_q3

import androidx.lifecycle.ViewModel

class TemperatureViewModel : ViewModel() {
    //
    // hold last 20 readings
    private val _temperatureReadings= MutableStateFlow<List<Float>>(emptyList())
    val temperatureReadings: StateFlow<List<Float>> = _temperatureReadings
    //control weather simulation
    private val _isRunning = MutableStateFlow(true)
    val isRunning: StateFlow<Boolean> = _isRunning

    /**
     * init block is executed when the ViewModel instance is created.
     */
    init {
       startTemperatureSimulation()
    }

    private fun startTemperatureSimulation(){
        viewModelScope.launch {
            while(true){
                if(_isRunning.value){
                    // generates random temperature between 65 and 85
                    val newTemp= Random.nextFloat()* 20f +65f
                    //adding to lst to keep last 20
                    val updated=(_temperatureReadings.value + newTemp).takeLast(20)
                    _temperatureReadings.value=updated
                    delay(2000)
                }
            }
        }
    }

    fun pause(){
        _isRunning.value=false
    }

    fun resume(){
        _isRunning.value=true

    }
    // summary calculations
    val current: Float?
        get()=temperatureReadings.value.lastOrNull()
    val average: Float?
        get() = temperatureReadings.value.takeIf { it.isNotEmpty() }?.average()?.toFloat()

    val min: Float?
        get() = temperatureReadings.value.minOrNull()

    val max: Float?
        get() = temperatureReadings.value.maxOrNull()

}
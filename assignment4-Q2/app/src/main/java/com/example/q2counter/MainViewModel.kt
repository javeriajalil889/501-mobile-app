package com.example.q2counter // Package name for the application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope // Provides a CoroutineScope tied to the ViewModel's lifecycle
import kotlinx.coroutines.delay // Suspending function to pause coroutine execution
import kotlinx.coroutines.flow.Flow // Base type for asynchronous data streams
import kotlinx.coroutines.flow.MutableSharedFlow // A SharedFlow that allows emitting values
import kotlinx.coroutines.flow.MutableStateFlow // A StateFlow that allows updating its value
import kotlinx.coroutines.flow.StateFlow // A flow that represents a read-only state
import kotlinx.coroutines.flow.asSharedFlow // Converts MutableSharedFlow to an immutable SharedFlow
import kotlinx.coroutines.flow.asStateFlow // Converts MutableStateFlow to an immutable StateFlow
import kotlinx.coroutines.flow.flow // Builder for creating cold Flows
import kotlinx.coroutines.launch // Launches a new coroutine without blocking the current thread

/**
 * ViewModel for Lecture4_Example2.
 * This class holds and manages UI-related data in a lifecycle-conscious way.
 * It demonstrates the use of Flow, StateFlow, and SharedFlow.
 */
class MainViewModel : ViewModel() {

    // --- Simple Flow (Cold Flow) ---
    /**
     * A simple cold Flow that emits integers from 1 to 5 with a 1-second delay between emissions.
     * "Cold" means this flow starts emitting values only when a terminal operator (like collect) is applied to it.
     * Each new collector will trigger the execution of the flow builder block independently.
     */
    val simpleFlow: Flow<Int> = flow {
        println("ViewModel Log: Simple flow builder started") // Log for observing flow behavior
        for (i in 1..5) {
            delay(1000) // Suspend for 1 second (simulates work)
            emit(i)     // Emit the current integer value
            println("ViewModel Log: Simple flow emitted $i")
        }
        println("ViewModel Log: Simple flow finished emitting.")
    }

    // --- StateFlow (Hot Flow for UI State) ---
    // Internally mutable state, publicly exposed as immutable StateFlow.
    private val _counterStateFlow = MutableStateFlow(0) // Initial value of the counter is 0.

    /**
     * A StateFlow representing a counter's current value.
     * "Hot" means it's active and holds a value even if there are no collectors.
     * New collectors immediately receive the current state.
     * It's designed to hold UI state and efficiently update observers.
     * Only distinct values are emitted.
     */
    val counterStateFlow: StateFlow<Int> = _counterStateFlow.asStateFlow()

    //auto mode flag
    private val _isAutoMode=MutuableStateFlow(false)
    val isAutoMode= _isAutoMode.asStateFlow()

    //function to toggle auto mode
    fun toggleAutoMode(){
        _isAutoMode.value=!_isAutoMode.value
    }

    /**
     * Increments the value of the counter held by [_counterStateFlow].
     * Observers of [counterStateFlow] will be notified of the new value.
     */
    fun incrementCounter() {
        _counterStateFlow.value += 1
        // Alternative update for more complex logic: _counterStateFlow.update { currentValue -> currentValue + 1 }
        println("ViewModel Log: StateFlow counter incremented to ${_counterStateFlow.value}")
    }


    fun decrementCounter() {
        _counterStateFlow.value -= 1
        println("ViewModel Log: StateFlow counter decremented to ${_counterStateFlow.value}")
    }

    fun resetCounter(){
        _counterStateFlow.value=0
    }

    // --- SharedFlow (Hot Flow for Events) ---
    // Internally mutable SharedFlow, publicly exposed as immutable SharedFlow.
    // `replay = 0` by default (no replay for new collectors).
    // `extraBufferCapacity = 0` by default.
    private val _eventSharedFlow = MutableSharedFlow<String>() // For one-time events like Snackbar messages

    /**
     * A SharedFlow used to broadcast one-time events (as Strings) to its collectors.
     * "Hot" means it can emit values even if there are no collectors.
     * Unlike StateFlow, it doesn't have an initial value or necessarily hold the last emitted value (unless configured with replay).
     * Ideal for events that should be consumed once, like showing a Toast/Snackbar or navigation.
     */
    val eventSharedFlow: Flow<String> = _eventSharedFlow.asSharedFlow() // Expose as Flow for more general usage if needed

    /**
     * Emits a message (event) to the [_eventSharedFlow].
     * This is done within the [viewModelScope] to ensure the coroutine is managed by the ViewModel's lifecycle.
     * @param message The string message to emit as an event.
     */
    fun sendEvent(message: String) {
        viewModelScope.launch { // Launch a coroutine in the ViewModel's scope
            _eventSharedFlow.emit(message) // Suspending call to emit the event
            println("ViewModel Log: SharedFlow event emitted - '$message'")
        }
    }

    /**
     * init block is executed when the ViewModel instance is created.
     */
    init {
        println("ViewModel Log: MainViewModel initialized")
        viewModelScope.launch(){
            while (true){
                if(_isAutoMode.value){
                    _counterStateFlow.value+=1
                }
                delay(3000) //delays 3 seconds
            }
        }
    }

    /**
     * This method is called when the ViewModel is no longer used and will be destroyed.
     * Good place to cancel any long-running coroutines not managed by viewModelScope (though viewModelScope usually handles this).
     */
    override fun onCleared() {
        super.onCleared()
        println("ViewModel Log: MainViewModel cleared")
    }
}
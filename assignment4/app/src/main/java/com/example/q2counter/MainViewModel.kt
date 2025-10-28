package com.example.q2counter
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

class MainViewModel : ViewModel() {


    val simpleFlow: Flow<Int> = flow {
        println("ViewModel Log: Simple flow builder started") // Log for observing flow behavior
        for (i in 1..5) {
            delay(1000) // Suspend for 1 second (simulates work)
            emit(i)     // Emit the current integer value
            println("ViewModel Log: Simple flow emitted $i")
        }
        println("ViewModel Log: Simple flow finished emitting.")
    }

    private val _counterStateFlow = MutableStateFlow(0) // Initial value of the counter is 0.
    val counterStateFlow: StateFlow<Int> = _counterStateFlow.asStateFlow()

    /**
     * Increments the value of the counter held by [_counterStateFlow].
     * Observers of [counterStateFlow] will be notified of the new value.
     */
    fun incrementCounter() {
        _counterStateFlow.value += 1
        // Alternative update for more complex logic: _counterStateFlow.update { currentValue -> currentValue + 1 }
        println("ViewModel Log: StateFlow counter incremented to ${_counterStateFlow.value}")
    }

    private val _eventSharedFlow = MutableSharedFlow<String>() // For one-time events like Snackbar messages


    val eventSharedFlow: Flow<String> = _eventSharedFlow.asSharedFlow() // Expose as Flow for more general usage if needed

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
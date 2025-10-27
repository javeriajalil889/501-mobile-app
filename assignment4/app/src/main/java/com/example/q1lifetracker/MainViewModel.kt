package com.example.q1lifetracker

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Define the data structure for a log entry.
data class LogEntry(
    val eventName: String,
    val timestamp: String,
    val color: Color
)

class MainViewModel : ViewModel() {
    private val _showSnackBar= MutableStateFlow(true)
    val showSnackBar= _showSnackBar.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    // A private mutable state flow to hold the list of logs.
    // Only the ViewModel can modify this list.
    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())

    // A public state flow that UI can observe for changes.
    // This is read-only to the UI.
    val logs: StateFlow<List<LogEntry>> = _logs

    // Function to get the current time as a formatted string.
    private fun getCurrentTimestamp(): String {
        return SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
    }

    // Function to assign a color based on the lifecycle event name.
    private fun getColorForEvent(event: String): Color {
        return when (event) {
            "onCreate", "onDestroy" -> Color.Red
            "onStart", "onStop" -> Color.Magenta
            "onResume", "onPause" -> Color(0xFFFFA500) // Orange
            else -> Color.Black
        }
    }

    // Function to add a new log entry to the list.
    fun addLog(eventName: String) {
        val newLog = LogEntry(
            eventName = eventName,
            timestamp = getCurrentTimestamp(),
            color = getColorForEvent(eventName)
        )
        if (_showSnackBar.value){
            _snackbarMessage.value="LifeCycle Event : $eventName"
        }
        // Update the state flow with the new list of logs.
        _logs.value = _logs.value + newLog
    }
    fun onShowSnackbarChanged(show: Boolean) {
        _showSnackBar.value = show
    }

    // Called by the UI after a snackbar has been shown, to clear the message.
    fun onSnackbarShown() {
        _snackbarMessage.value = null
    }
}
    
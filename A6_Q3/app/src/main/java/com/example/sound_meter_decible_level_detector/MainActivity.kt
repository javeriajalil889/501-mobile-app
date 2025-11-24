package com.example.sound_meter_decible_level_detector

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.math.log10
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var meterBar: ProgressBar
    //text that will display dB value
    private lateinit var dbText: TextView
    private val thresholdDb = 80 //noise threshold

    private var audioRecord: AudioRecord? = null
    private var isRecording = false
    private lateinit var recordingThread: Thread

    companion object {
        private const val AUDIO_PERMISSION_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        meterBar = findViewById(R.id.meterBar)
        dbText = findViewById(R.id.dbText)

        //if given persmission, start meter
        if (checkAudioPermission()) {
            startMeter()
        } else {
            requestAudioPermission()
        }
    }
//this function is called to check permission
    private fun checkAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }
//this function is called to request permission
    private fun requestAudioPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            AUDIO_PERMISSION_CODE
        )
    }
  //this function is called when user grants or denies permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == AUDIO_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMeter()
            } else {
                dbText.text = "Permission Denied"
            }
        }
    }
//this function is called to start meter
private fun startMeter() {
    val sampleRate = 44100
    val channelConfig = AudioFormat.CHANNEL_IN_MONO
    val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    val minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        != PackageManager.PERMISSION_GRANTED) return

    audioRecord = AudioRecord(
        MediaRecorder.AudioSource.MIC,
        sampleRate,
        channelConfig,
        audioFormat,
        minBufferSize
    )

    val buffer = ShortArray(minBufferSize)
    audioRecord?.startRecording()
    isRecording = true

    recordingThread = Thread {
        while (isRecording) {
            val readSize = audioRecord?.read(buffer, 0, buffer.size) ?: 0
            if (readSize > 0) {
                val rms = buffer.take(readSize).map { it.toDouble() * it.toDouble() }.average()
                val amplitude = sqrt(rms)
                val db = if (amplitude > 0) 20 * log10(amplitude) else 0.0

                runOnUiThread {
                    if (isFinishing || isDestroyed) return@runOnUiThread
                    dbText.text = String.format("%.1f dB", db)
                    meterBar.progress = db.toInt().coerceIn(0, 120)

                    val textColor: Int
                    if (db > thresholdDb) {
                        textColor = ContextCompat.getColor(this, android.R.color.holo_red_light)
                        Toast.makeText(this, "Noise level is too high!", Toast.LENGTH_SHORT).show()
                    } else {
                        textColor = ContextCompat.getColor(this, android.R.color.white)
                    }
                    dbText.setTextColor(textColor)
                }
            }
        }
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }
    recordingThread.start()
}


    override fun onPause() {
        super.onPause()
        isRecording = false
    }

    override fun onResume() {
        super.onResume()
        if (checkAudioPermission() && audioRecord == null) {
            startMeter()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRecording = false
        try {
            recordingThread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}

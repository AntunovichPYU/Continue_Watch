package ru.spbstu.icc.kspt.lab2.continuewatch

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivityAlt : AppCompatActivity() {
    private var secondsElapsed: Int = 0
    private lateinit var textSecondsElapsed: TextView
    private var isRunning = true
    private lateinit var sharedPref: SharedPreferences

    private var backgroundThread = Thread {
        while (true) {
            textSecondsElapsed.post {
                textSecondsElapsed.text =
                    if (isRunning) getString(R.string.secondsElapsed, secondsElapsed++)
                    else getString(R.string.secondsElapsed, secondsElapsed)
            }
            Thread.sleep(1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        backgroundThread.start()
    }

    override fun onResume() {
        isRunning = true
        sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val defaultValue = resources.getInteger(R.integer.seconds_elapsed_default_key)
        secondsElapsed = sharedPref.getInt(getString(R.string.secondsElapsed), defaultValue)

        super.onResume()
    }

    override fun onPause() {
        saveOnSharedPref()
        isRunning = false
        super.onPause()
    }

    override fun onDestroy() {
        saveOnSharedPref()
        isRunning = false
        super.onDestroy()
    }

    private fun saveOnSharedPref() {
        with (sharedPref.edit()) {
            putInt(getString(R.string.secondsElapsed), secondsElapsed)
            apply()
        }
    }
}
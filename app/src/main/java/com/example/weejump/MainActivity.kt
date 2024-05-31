package com.example.weejump

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private lateinit var accelerometerHandler: AccelerometerHandler
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var playSoundButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the AccelerometerHandler
        accelerometerHandler = AccelerometerHandler(this)

        // Initialize the MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.weee) // Replace R.raw.jump_sound with the resource ID of your sound file

        // Find the play_sound_button view
        playSoundButton = findViewById(R.id.play_sound_button)

        // Register the accelerometer sensor listener
        accelerometerHandler.registerAccelerometerListener()

        // Set the onJumpDetected listener
        accelerometerHandler.setOnHighVelocityDetectedListener {
            // Trigger the sound when a jump is detected
            mediaPlayer.start()
        }

        // Set the onClick listener for the play_sound_button
        playSoundButton.setOnClickListener {
            // Trigger the sound when the button is clicked
            mediaPlayer.start()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the MediaPlayer resources when the app is destroyed
        mediaPlayer.release()

        // Unregister the accelerometer sensor listener
        accelerometerHandler.unregisterAccelerometerListener()
    }
}

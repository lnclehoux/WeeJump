package com.example.weejump

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class AccelerometerHandler(context: Context) : SensorEventListener {
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var accelerationData: DoubleArray = DoubleArray(3)
    private var onHighVelocityDetected: (() -> Unit)? = null
    private val highVelocityThreshold = 30.0

    fun registerAccelerometerListener() {
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun unregisterAccelerometerListener() {
        if (accelerometer != null) {
            sensorManager.unregisterListener(this)
        }
    }

    fun setOnHighVelocityDetectedListener(listener: () -> Unit) {
        onHighVelocityDetected = listener
    }

    fun getAccelerationData(): DoubleArray {
        return accelerationData
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            accelerationData = event.values.map { it.toDouble() }.toDoubleArray()

            // Calculate the magnitude of the acceleration
            val accelerationMagnitude = sqrt(
                accelerationData[0] * accelerationData[0] +
                        accelerationData[1] * accelerationData[1] +
                        accelerationData[2] * accelerationData[2]
            )

            // Check if the magnitude exceeds the high-velocity threshold
            if (accelerationMagnitude >= highVelocityThreshold) {
                // Call the onHighVelocityDetected listener
                onHighVelocityDetected?.invoke()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

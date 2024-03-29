package com.example.method_chanel

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.flutter.plugin.common.EventChannel

class StreamHandler(private val sensorManager: SensorManager,sensorType: Int, private  var interval:Int = SensorManager.SENSOR_DELAY_NORMAL):
    EventChannel.StreamHandler,SensorEventListener {
    private val sensor = sensorManager.getDefaultSensor(sensorType)
    private  var eventSink:EventChannel.EventSink? = null
    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
       // TODO("Not yet implemented")
        if(sensor!= null){
            eventSink = events
            sensorManager.registerListener(this,sensor,interval)
        }
    }

    override fun onCancel(arguments: Any?) {
       // TODO("Not yet implemented")
        sensorManager.unregisterListener(this)
        eventSink = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
      //  TODO("Not yet implemented")
        val sensorValues = event!!.values[0]
        println(sensorValues)
        eventSink!!.success(sensorValues)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
       // TODO("Not yet implemented")
    }
}
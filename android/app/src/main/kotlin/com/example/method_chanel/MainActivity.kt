package com.example.method_chanel


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {

    private val METHOD_CHANNEL_NAME = "com.julow.barometer/method"
    private val PRESSURE_CHANNEL_NAME = "com.julow.barometer/pressure"
    private var methodChannel:MethodChannel?= null
    private lateinit var sensorManager: SensorManager

    override fun configureFlutterEngine(@NonNull  flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        setUpChannels(this,flutterEngine.dartExecutor.binaryMessenger)
    }
    override fun onDestroy(){
        tearDownChannels()
        super.onDestroy()
    }
    private fun  setUpChannels(context:Context,messenger: BinaryMessenger){
         sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
         methodChannel = MethodChannel(messenger,METHOD_CHANNEL_NAME)
         methodChannel!!.setMethodCallHandler {
                call, result ->
             if(call.method == "isSensorAvailable"){
                 result.success(sensorManager!!.getSensorList(Sensor.TYPE_PRESSURE).isNotEmpty())
             }
             else{
                 result.notImplemented()
             }
        }
    }
    private fun tearDownChannels(){
        methodChannel!!.setMethodCallHandler(null)
    }

}

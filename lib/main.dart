import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
      ),
      home: const MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {

  static const methodChannel = MethodChannel('com.julow.barometer/method');
  static const pressureChannel = EventChannel('com.julow.barometer/pressure');
  String _sensorAvailable='Unknown';
  double _pressureReading = 0;
  StreamSubscription? pressureSubscription;
  Future<void> isAvailability()async{
      try{
        var available = await methodChannel.invokeMethod('isSensorAvailable');
        setState(() {
          _sensorAvailable=available.toString();
        });
      }on PlatformException catch(e){
        print(e);
      }
  }
  _startReading(){
     pressureSubscription = pressureChannel.receiveBroadcastStream().listen((event) {
       setState(() {
         _pressureReading = event;
       });
     });
  }
  _stopReading(){
    setState(() {
      _pressureReading = 0;
    });
    pressureSubscription!.cancel();
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(

        backgroundColor: Colors.purple,

        title: const Text("Method Channel"),
      ),
      body: Center(

        child: Column(

          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
             Text(
              'Sensor Available? : $_sensorAvailable',
            ),
            ElevatedButton(
                onPressed:()=> isAvailability(),
                child: Text(
                  'Check Sensor Available',
                ),
            ),
            SizedBox(height: 50,),
            if(_pressureReading!=0)
            Text(
              'Sensor reading: $_pressureReading',
            ),
            if(_sensorAvailable == 'true' && _pressureReading==0)
            ElevatedButton(
              onPressed:()=> _startReading(),
              child: Text(
                'Start reading',
              ),
            ),
            if(_pressureReading!=0)
            ElevatedButton(
              onPressed:()=> _stopReading(),
              child: Text(
                'Stop reading',
              ),
            ),

          ],
        ),
      ),

    );
  }
}

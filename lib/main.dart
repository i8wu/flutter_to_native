import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Channel Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MyHomePage(title: 'Flutter Channel Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = const MethodChannel('flutter.native/helper');
  String _responseFromNativeCode = 'Waiting...';
  String _name = '';

  Future<void> _callNativeCode() async {
    String response = '';
    try {
      final String result = await platform.invokeMethod('helloFromNativeCode', { 'name': _name });
      response = result;
    } on PlatformException catch (e) {
      response = 'Failed: ${e.message}';
    }

    setState(() {
      _responseFromNativeCode = response;
    });
  }

  void _setName (text) {
    setState(() {
      _name = text;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'Type name and click button to get response from native code:',
            ),
            TextField(
              onChanged: _setName,
            ),
            Text(
              '$_responseFromNativeCode',
              style: Theme.of(context).textTheme.headline4,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _callNativeCode,
        tooltip: 'Call Native',
        child: Icon(Icons.add),
      ),
    );
  }
}

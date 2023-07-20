import 'package:do_not_disturb/do_not_disturb.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'DND',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Do Not Disturb Example'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final DoNotDisturb _doNotDisturb = DoNotDisturb();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: StreamBuilder<bool>(
        stream: _doNotDisturb.statusAsStream,
        builder: (context, snapshot) {
          if (snapshot.hasError) {
            return Text(
              'Error: ${snapshot.error}',
              style: Theme.of(context).textTheme.headlineMedium,
            );
          }
          if (!snapshot.hasData) {
            return Text(
              'Loading...',
              style: Theme.of(context).textTheme.headlineMedium,
            );
          }

          final value = snapshot.data!;

          return SizedBox(
            width: double.infinity,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: <Widget>[
                const Divider(),
                const SizedBox(height: 20),
                Text(
                  'DND Enabled: $value',
                  style: Theme.of(context).textTheme.headlineMedium,
                ),
                const SizedBox(height: 10),
                Switch(
                  value: value,
                  onChanged: (value) async {
                    await _doNotDisturb.setStatus(value);
                    setState(() {});
                  },
                ),
                const SizedBox(height: 20),
                StreamBuilder<bool>(
                    stream: _doNotDisturb.isPermissionGranted.asStream(),
                    builder: (context, snapshot) {
                      if (snapshot.hasError) {
                        return Text(
                          'Error: ${snapshot.error}',
                          style: Theme.of(context).textTheme.headlineMedium,
                        );
                      }
                      if (!snapshot.hasData) {
                        return Text(
                          'Loading...',
                          style: Theme.of(context).textTheme.headlineMedium,
                        );
                      }

                      final permissionVal = snapshot.data!;
                      return Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text('Permission Granted: $permissionVal'),
                          const SizedBox(width: 10),
                          IconButton(
                            onPressed: () async {
                              await _doNotDisturb.openDoNotDisturbSettings();
                            },
                            icon: const Icon(Icons.settings),
                          ),
                        ],
                      );
                    }),
                const Divider()
              ],
            ),
          );
        },
      ),
      //test openDoNotDisturbSettings
      floatingActionButton: FloatingActionButton(
        onPressed: () async {
          setState(
            () async {},
          );
        },
        tooltip: 'Refresh',
        child: const Icon(Icons.refresh),
      ),
    );
  }
}

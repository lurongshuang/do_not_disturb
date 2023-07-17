import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'do_not_disturb_platform_interface.dart';

/// An implementation of [DoNotDisturbPlatform] that uses method channels.
class MethodChannelDoNotDisturb extends DoNotDisturbPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('do_not_disturb');

  MethodChannelDoNotDisturb() {
    _inint();
  }

  @override
  Future<void> setStatus(bool value) async {
    try {
      await methodChannel.invokeMethod('setStatus', value);
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print('Error setting Do Not Disturb: ${e.message}');
      }
      rethrow;
    }
  }

  Future<bool> _status() async {
    try {
      final bool status = await methodChannel.invokeMethod('status');
      return status;
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print('Error getting Do Not Disturb status: ${e.message}');
      }
      rethrow;
    }
  }

  @override
  Future<bool> get status async => await _status();

  static const EventChannel _eventChannel =
      EventChannel('do_not_disturb/status');
  @override
  Stream<bool> statusStream() {
    try {
      return _eventChannel
          .receiveBroadcastStream()
          .map((event) => event as bool);
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print('Error getting Do Not Disturb status stream: ${e.message}');
      }
      rethrow;
    }
  }

  @override
  openDoNotDisturbSettings() async {
    try {
      await methodChannel.invokeMethod('openDoNotDisturbSettings');
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print('Error opening Do Not Disturb settings: ${e.message}');
      }
      rethrow;
    }
  }

  _inint() async {
    try {
      await methodChannel.invokeMethod('enableDndNotificationListener');
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print('Error enabling listiner: ${e.message}');
      }
      rethrow;
    }
  }

  @override
  Future<void> dispose() async {
    await _eventChannel.receiveBroadcastStream().drain();
  }
}

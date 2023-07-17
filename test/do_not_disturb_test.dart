import 'package:flutter_test/flutter_test.dart';
import 'package:do_not_disturb/do_not_disturb_platform_interface.dart';
import 'package:do_not_disturb/do_not_disturb_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockDoNotDisturbPlatform
    with MockPlatformInterfaceMixin
    implements DoNotDisturbPlatform {
  @override
  Future<void> setStatus(bool value) {
    // TODO: implement setStatus
    throw UnimplementedError();
  }

  @override
  // TODO: implement status
  Future<bool> get status => throw UnimplementedError();

  @override
  Stream<bool> statusStream() {
    // TODO: implement statusStream
    throw UnimplementedError();
  }

  @override
  Future<void> dispose() {
    // TODO: implement dispose
    throw UnimplementedError();
  }

  @override
  openDoNotDisturbSettings() {
    // TODO: implement openDoNotDisturbSettings
    throw UnimplementedError();
  }
}

void main() {
  final DoNotDisturbPlatform initialPlatform = DoNotDisturbPlatform.instance;

  test('$MethodChannelDoNotDisturb is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelDoNotDisturb>());
  });

  test('getPlatformVersion', () async {
    MockDoNotDisturbPlatform fakePlatform = MockDoNotDisturbPlatform();
    DoNotDisturbPlatform.instance = fakePlatform;
  });
}

import 'package:flutter_test/flutter_test.dart';
import 'package:do_not_disturb/do_not_disturb.dart';
import 'package:do_not_disturb/do_not_disturb_platform_interface.dart';
import 'package:do_not_disturb/do_not_disturb_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockDoNotDisturbPlatform
    with MockPlatformInterfaceMixin
    implements DoNotDisturbPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final DoNotDisturbPlatform initialPlatform = DoNotDisturbPlatform.instance;

  test('$MethodChannelDoNotDisturb is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelDoNotDisturb>());
  });

  test('getPlatformVersion', () async {
    DoNotDisturb doNotDisturbPlugin = DoNotDisturb();
    MockDoNotDisturbPlatform fakePlatform = MockDoNotDisturbPlatform();
    DoNotDisturbPlatform.instance = fakePlatform;

    expect(await doNotDisturbPlugin.getPlatformVersion(), '42');
  });
}

import 'do_not_disturb_platform_interface.dart';

class DoNotDisturb {
  Future<String?> getPlatformVersion() {
    return DoNotDisturbPlatform.instance.getPlatformVersion();
  }

  Future<bool> setEnabled(bool enabled) {
    return DoNotDisturbPlatform.instance.setStatus(enabled);
  }
}

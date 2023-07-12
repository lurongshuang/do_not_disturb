import 'do_not_disturb_platform_interface.dart';

class DoNotDisturb {
  Future<bool> setStatus(bool enabled) {
    return DoNotDisturbPlatform.instance.setStatus(enabled);
  }

  Future<bool> get status async => await DoNotDisturbPlatform.instance.status;

  Stream<bool> get statusAsStream =>
      DoNotDisturbPlatform.instance.statusStream();
}

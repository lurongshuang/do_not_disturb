import 'do_not_disturb_method_channel.dart';

class DoNotDisturb {
  final MethodChannelDoNotDisturb dnd = MethodChannelDoNotDisturb();
  Future<String?> getPlatformVersion() {
    return dnd.getPlatformVersion();
  }

  Future<bool> setStatus(bool enabled) {
    return dnd.setStatus(enabled);
  }

  Future<bool> get status async => await dnd.status;

  Stream<bool> get onEnabledStatusChanged {
    return dnd.onStatusChanged;
  }
}

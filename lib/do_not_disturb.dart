import 'package:permission_handler/permission_handler.dart';

import 'do_not_disturb_platform_interface.dart';

class DoNotDisturb {
  Future<void> setStatus(bool enabled) async {
    final status = await _requestNotificationPolicyAccess();
    if (!status) {
      throw Exception('Notification Policy Access Denied');
    }
    await DoNotDisturbPlatform.instance.setStatus(enabled);
  }

  Future<bool> get status async => await DoNotDisturbPlatform.instance.status;

  Stream<bool> get statusAsStream =>
      DoNotDisturbPlatform.instance.statusStream();

  Future<bool> get isPermissionGranted async =>
      await Permission.accessNotificationPolicy.isGranted;

  Future<bool> _requestNotificationPolicyAccess() async {
    final isGranted = await isPermissionGranted;
    if (!isGranted) {
      final PermissionStatus status =
          await Permission.accessNotificationPolicy.request();
      if (status.isDenied || status.isPermanentlyDenied) {
        return false;
      }
    }

    return true;
  }

  openDoNotDisturbSettings() async {
    await DoNotDisturbPlatform.instance.openDoNotDisturbSettings();
  }

  Future<void> dispose() async {
    await DoNotDisturbPlatform.instance.dispose();
  }
}

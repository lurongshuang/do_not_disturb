import Flutter
import UIKit
import UserNotifications


public class DoNotDisturbPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "do_not_disturb", binaryMessenger: registrar.messenger())
    let instance = DoNotDisturbPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    if call.method == "setStatus" {
      if let args = call.arguments as? Bool {
                setDoNotDisturb(value: args)

        result(nil)
      } else {
        result(FlutterError(code: "INVALID_ARGUMENTS", message: "Invalid arguments", details: nil))
      }
    } 
    if call.method == "status" {
      if UIApplication.shared.isIgnoringInteractionEvents {
      result(true)
} else {
      result(false)
}
    }
    else {
      result(FlutterMethodNotImplemented)
    }
  }



func setDoNotDisturb2(value: Bool) {
  // let settings = UNNotificationSettings(    
  //   showAlerts: false,
  //   showBadges: false,
  //   playSounds: false
  // )

  UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]) { granted, error in
    if granted {
    let n =  UNUserNotificationCenter.current()
    // .setNotificationSettings(settings)
    print(n);
    } else {
      print("User has declined notifications")
    }
  }

}


func setDoNotDisturb(value: Bool) {
    let notificationCenter = UNUserNotificationCenter.current()

    notificationCenter.getNotificationSettings { settings in
        if settings.authorizationStatus == .authorized {
            let options: UNAuthorizationOptions = value ? [.alert, .sound, .badge, .providesAppNotificationSettings] : []
            notificationCenter.requestAuthorization(options: options) { granted, error in
                if let error = error {
                    print("Error requesting notification authorization: \(error.localizedDescription)")
                }
            }
        }
    }
}



}



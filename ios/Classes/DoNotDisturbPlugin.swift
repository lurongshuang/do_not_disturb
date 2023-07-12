import Flutter
import UIKit

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
    if call.method == "getStatus" {
      // later
      result(false)
    }
    else {
      result(FlutterMethodNotImplemented)
    }
  }

  func setDoNotDisturb(value: Bool) {
    // Native code to set "Do Not Disturb" mode on iOS
    let notificationCenter = UNUserNotificationCenter.current()
    // Rest of the implementation...
  }
}

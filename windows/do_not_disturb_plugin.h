#ifndef FLUTTER_PLUGIN_DO_NOT_DISTURB_PLUGIN_H_
#define FLUTTER_PLUGIN_DO_NOT_DISTURB_PLUGIN_H_

#include <flutter/method_channel.h>
#include <flutter/plugin_registrar_windows.h>

#include <memory>

namespace do_not_disturb {

class DoNotDisturbPlugin : public flutter::Plugin {
 public:
  static void RegisterWithRegistrar(flutter::PluginRegistrarWindows *registrar);

  DoNotDisturbPlugin();

  virtual ~DoNotDisturbPlugin();

  // Disallow copy and assign.
  DoNotDisturbPlugin(const DoNotDisturbPlugin&) = delete;
  DoNotDisturbPlugin& operator=(const DoNotDisturbPlugin&) = delete;

  // Called when a method is called on this plugin's channel from Dart.
  void HandleMethodCall(
      const flutter::MethodCall<flutter::EncodableValue> &method_call,
      std::unique_ptr<flutter::MethodResult<flutter::EncodableValue>> result);
};

}  // namespace do_not_disturb

#endif  // FLUTTER_PLUGIN_DO_NOT_DISTURB_PLUGIN_H_

#include "include/do_not_disturb/do_not_disturb_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "do_not_disturb_plugin.h"

void DoNotDisturbPluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  do_not_disturb::DoNotDisturbPlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}

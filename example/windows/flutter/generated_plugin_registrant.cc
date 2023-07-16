//
//  Generated file. Do not edit.
//

// clang-format off

#include "generated_plugin_registrant.h"

#include <do_not_disturb/do_not_disturb_plugin_c_api.h>
#include <permission_handler_windows/permission_handler_windows_plugin.h>

void RegisterPlugins(flutter::PluginRegistry* registry) {
  DoNotDisturbPluginCApiRegisterWithRegistrar(
      registry->GetRegistrarForPlugin("DoNotDisturbPluginCApi"));
  PermissionHandlerWindowsPluginRegisterWithRegistrar(
      registry->GetRegistrarForPlugin("PermissionHandlerWindowsPlugin"));
}

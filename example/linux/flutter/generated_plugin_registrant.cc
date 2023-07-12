//
//  Generated file. Do not edit.
//

// clang-format off

#include "generated_plugin_registrant.h"

#include <do_not_disturb/do_not_disturb_plugin.h>

void fl_register_plugins(FlPluginRegistry* registry) {
  g_autoptr(FlPluginRegistrar) do_not_disturb_registrar =
      fl_plugin_registry_get_registrar_for_plugin(registry, "DoNotDisturbPlugin");
  do_not_disturb_plugin_register_with_registrar(do_not_disturb_registrar);
}

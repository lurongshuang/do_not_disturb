package com.geeksaxis.do_not_disturb
import androidx.annotation.NonNull
import android.app.NotificationManager
import android.app.NotificationManager.INTERRUPTION_FILTER_NONE
import android.content.Context
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding.OnSaveInstanceStateListener
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding.OnStartListener
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding.OnStopListener
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding.OnRestoreInstanceStateListener
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

class DoNotDisturbPlugin : FlutterPlugin, MethodCallHandler, OnStartListener, OnStopListener,
    OnSaveInstanceStateListener, OnRestoreInstanceStateListener {

  private lateinit var channel: MethodChannel
  private lateinit var applicationContext: Context

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "do_not_disturb")
    channel.setMethodCallHandler(this)
    applicationContext = flutterPluginBinding.applicationContext
  }

  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "do_not_disturb")
      val plugin = DoNotDisturbPlugin()
      plugin.applicationContext = registrar.context().applicationContext
      channel.setMethodCallHandler(plugin)
      registrar.addStartListener(plugin)
      registrar.addStopListener(plugin)
      registrar.addSaveStateListener(plugin)
      registrar.addRestoreStateListener(plugin)
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "setStatus") {
      if (call.arguments is Boolean) {
        setDoNotDisturbEnabled(call.arguments as Boolean)
        result.success(null)
      } else {
        result.error("INVALID_ARGUMENTS", "Invalid arguments", null)
      }
    } 
    if(call.method = "status"){
       result.success(true)
    }
    else {
      result.notImplemented()
    }
  }

  private fun setDoNotDisturbEnabled(enabled: Boolean) {
    val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (enabled) {
      notificationManager.setInterruptionFilter(INTERRUPTION_FILTER_NONE)
    } else {
      // Restore the previous interruption filter (optional)
      notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onStart() {
    // Optional: Handle onStart event
  }

  override fun onStop() {
    // Optional: Handle onStop event
  }

  override fun onSaveInstanceState(): Any? {
    // Optional: Handle onSaveInstanceState event
    return null
  }

  override fun onRestoreInstanceState(state: Any?) {
    // Optional: Handle onRestoreInstanceState event
  }
}

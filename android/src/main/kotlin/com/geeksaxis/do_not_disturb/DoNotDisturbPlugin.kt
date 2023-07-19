package com.geeksaxis.do_not_disturb

import android.app.NotificationManager
import android.app.NotificationManager.INTERRUPTION_FILTER_NONE
import android.content.Context
import android.content.Intent
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class DoNotDisturbPlugin : FlutterPlugin, MethodCallHandler {

    private lateinit var channel: MethodChannel
    private lateinit var applicationContext: Context
    private lateinit var notificationManager: NotificationManager

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "do_not_disturb")
        channel.setMethodCallHandler(this)
        applicationContext = flutterPluginBinding.applicationContext

        notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "setStatus" -> {
                val enabled = call.arguments as? Boolean
                if (enabled != null) {
                    setDoNotDisturbEnabled(enabled)
                    result.success(null)
                } else {
                    result.error("INVALID_ARGUMENTS", "Invalid arguments", null)
                }
            }
            "status" -> {
                getCurrentInterruptionFilter()
                result.success(null)
            }
            "openDoNotDisturbSettings" -> {
                openDoNotDisturbSettings()
                result.success(null)
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    private fun openDoNotDisturbSettings() {
        val intent = android.content.Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
    }

    private fun setDoNotDisturbEnabled(enabled: Boolean) {
        val filter = if (enabled) INTERRUPTION_FILTER_NONE else NotificationManager.INTERRUPTION_FILTER_ALL
        notificationManager.setInterruptionFilter(filter)
    }

    private fun getCurrentInterruptionFilter() {
        val filter = notificationManager.currentInterruptionFilter
        // Do something with the current filter status if needed
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}

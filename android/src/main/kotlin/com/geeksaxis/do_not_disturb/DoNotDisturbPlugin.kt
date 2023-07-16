package com.geeksaxis.do_not_disturb

import android.app.NotificationManager
import android.app.NotificationManager.INTERRUPTION_FILTER_NONE
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class DoNotDisturbPlugin : FlutterPlugin, MethodCallHandler {

    private lateinit var channel: MethodChannel
    private lateinit var applicationContext: Context

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "do_not_disturb")
        channel.setMethodCallHandler(this)
        applicationContext = flutterPluginBinding.applicationContext
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "setStatus" -> {
                if (call.arguments is Boolean) {
                    setDoNotDisturbEnabled(call.arguments as Boolean)
                    result.success(null)
                } else {
                    result.error("INVALID_ARGUMENTS", "Invalid arguments", null)
                }
            }
            "status" -> {
                result.success(true)
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setDoNotDisturbEnabled(enabled: Boolean) {
        val notificationManager = getSystemService(applicationContext, NotificationManager::class.java)
        if (enabled) {
            notificationManager?.setInterruptionFilter(INTERRUPTION_FILTER_NONE)
        } else {
            // Restore the previous interruption filter (optional)
            notificationManager?.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        }
    }


    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

}

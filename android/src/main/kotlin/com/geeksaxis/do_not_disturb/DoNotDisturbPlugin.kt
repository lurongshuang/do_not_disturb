package com.geeksaxis.do_not_disturb

import android.app.NotificationManager
import android.app.NotificationManager.INTERRUPTION_FILTER_NONE
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class DoNotDisturbPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var notificationManager: NotificationManager
    private lateinit var audioManager: AudioManager
    private lateinit var applicationContext: Context

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "do_not_disturb")
        channel.setMethodCallHandler(this)
        applicationContext = flutterPluginBinding.applicationContext
        notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
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
                
                result.success(getCurrentInterruptionFilter())
            }

            "openDoNotDisturbSettings"->{
                openDoNotDisturbSettings()
                result.success(null)
            }
            else -> {
                result.notImplemented()
            }
        }
    }
 private fun setDoNotDisturbEnabled(enabled: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setDoNotDisturbEnabledMinAndroidM(enabled)
        } else {
            setDoNotDisturbEnabledOlderThanAndroidM(enabled)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setDoNotDisturbEnabledMinAndroidM(enabled: Boolean) {
         val filter = if (enabled) INTERRUPTION_FILTER_NONE else NotificationManager.INTERRUPTION_FILTER_ALL
        if (filter == INTERRUPTION_FILTER_NONE) {
            audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
        } else {
            audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
        }
        notificationManager.setInterruptionFilter(filter)
    }

    private fun setDoNotDisturbEnabledOlderThanAndroidM(enabled: Boolean) {
         if (enabled) {
            audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
        } else {
            audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
        }
    }

    private fun getCurrentInterruptionFilter(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getCurrentInterruptionFilterMinAndroidM()
        } else {
            getCurrentInterruptionFilterOlderThanAndroidM()
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun getCurrentInterruptionFilterMinAndroidM(): Boolean {
        val filter = notificationManager.currentInterruptionFilter
        return filter == INTERRUPTION_FILTER_NONE
    }

    private fun getCurrentInterruptionFilterOlderThanAndroidM(): Boolean {
        val mode = audioManager.ringerMode
        return mode == AudioManager.RINGER_MODE_SILENT
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private  fun openDoNotDisturbSettings() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

}
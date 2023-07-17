package com.geeksaxis.do_not_disturb
import android.content.ComponentName
import android.content.pm.PackageManager

import android.app.NotificationManager
import android.app.NotificationManager.INTERRUPTION_FILTER_NONE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class DoNotDisturbPlugin : FlutterPlugin, MethodCallHandler {

    private lateinit var channel: MethodChannel
    private lateinit var applicationContext: Context
    private lateinit var eventChannel: EventChannel
    private lateinit var eventSink: EventChannel.EventSink
    private lateinit var notificationManager: NotificationManager
    private lateinit var audioManager: AudioManager
    private lateinit var interruptionFilterReceiver: BroadcastReceiver

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "do_not_disturb")
        channel.setMethodCallHandler(this)
        applicationContext = flutterPluginBinding.applicationContext

        eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "do_not_disturb/status")
        eventChannel.setStreamHandler(object : EventChannel.StreamHandler {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
                eventSink = events
                registerInterruptionFilterReceiver()
                getCurrentInterruptionFilter()
            }

            override fun onCancel(arguments: Any?) {
                unregisterInterruptionFilterReceiver()
            }
        })

        notificationManager = getSystemService(applicationContext, NotificationManager::class.java)!!
        audioManager = getSystemService(applicationContext, AudioManager::class.java)!!
    }

    @RequiresApi(Build.VERSION_CODES.M)
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
            "enableDndNotificationListener" -> {
                enableDndNotificationListener()
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
@RequiresApi(Build.VERSION_CODES.M)
private  fun openDoNotDisturbSettings() {
    val intent = Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    applicationContext.startActivity(intent)
}
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setDoNotDisturbEnabled(enabled: Boolean) {
        val filter = if (enabled) INTERRUPTION_FILTER_NONE else NotificationManager.INTERRUPTION_FILTER_ALL
        if (filter == INTERRUPTION_FILTER_NONE) {
            audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
        } else {
            audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
        }
        notificationManager.setInterruptionFilter(filter)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getCurrentInterruptionFilter() {
        val filter = notificationManager.currentInterruptionFilter
        val status = filter == INTERRUPTION_FILTER_NONE
        eventSink.success(status)
    }

   @RequiresApi(Build.VERSION_CODES.M)
private fun registerInterruptionFilterReceiver() {
    interruptionFilterReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "DND_STATUS_CHANGED") {
                getCurrentInterruptionFilter()
            }
        }
    }
    val filter = IntentFilter("DND_STATUS_CHANGED")
    applicationContext.registerReceiver(interruptionFilterReceiver, filter)

    // Enable the DND notification listener service
    enableDndNotificationListener()
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
private fun enableDndNotificationListener() {
    val componentName = ComponentName(applicationContext, DndNotificationListenerService::class.java)
    applicationContext.packageManager.setComponentEnabledSetting(
        componentName,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP
    )
}



    private fun unregisterInterruptionFilterReceiver() {
        applicationContext.unregisterReceiver(interruptionFilterReceiver)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        eventChannel.setStreamHandler(null)
        applicationContext.unregisterReceiver(interruptionFilterReceiver)
    }
}

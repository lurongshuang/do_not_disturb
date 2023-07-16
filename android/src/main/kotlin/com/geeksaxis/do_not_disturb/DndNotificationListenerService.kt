package com.geeksaxis.do_not_disturb

import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.annotation.RequiresApi
import io.flutter.Log

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class DndNotificationListenerService : NotificationListenerService() {

    override fun onListenerConnected() {
        Log.d("DndListenerService", "Notification listener connected")
    }

    override fun onListenerDisconnected() {
        Log.d("DndListenerService", "Notification listener disconnected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        // Send a broadcast to notify the Flutter app about the DND status change
        sendBroadcast(Intent("DND_STATUS_CHANGED"))
    }
}

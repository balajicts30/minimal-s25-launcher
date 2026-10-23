package com.example.minimals25

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class S25NotificationItem(
    val key: String,
    val packageName: String,
    val appName: String,
    val title: String,
    val text: String,
    val timestamp: Long,
    val pendingIntent: PendingIntent? = null
)

class S25NotificationService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        _isConnected.value = true
        refreshActiveNotifications()
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        _isConnected.value = false
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        refreshActiveNotifications()
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        refreshActiveNotifications()
    }

    private fun refreshActiveNotifications() {
        try {
            val active = activeNotifications ?: return
            val pm = packageManager
            val items = mutableListOf<S25NotificationItem>()

            for (sbn in active) {
                // Ignore ongoing or system silent noise
                if (sbn.isOngoing) continue

                val notif = sbn.notification ?: continue
                val extras = notif.extras ?: continue

                val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()?.trim() ?: ""
                val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()?.trim()
                    ?: extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString()?.trim()
                    ?: ""

                if (title.isBlank() && text.isBlank()) continue

                val appName = try {
                    val appInfo = pm.getApplicationInfo(sbn.packageName, 0)
                    pm.getApplicationLabel(appInfo).toString()
                } catch (_: PackageManager.NameNotFoundException) {
                    sbn.packageName
                }

                items.add(
                    S25NotificationItem(
                        key = sbn.key,
                        packageName = sbn.packageName,
                        appName = appName,
                        title = title,
                        text = text,
                        timestamp = sbn.postTime,
                        pendingIntent = notif.contentIntent
                    )
                )
            }

            _notificationsFlow.value = items.sortedByDescending { it.timestamp }.take(15)
        } catch (_: Exception) {
        }
    }

    companion object {
        private val _notificationsFlow = MutableStateFlow<List<S25NotificationItem>>(emptyList())
        val notificationsFlow: StateFlow<List<S25NotificationItem>> = _notificationsFlow.asStateFlow()

        private val _isConnected = MutableStateFlow(false)
        val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

        fun isNotificationAccessGranted(context: Context): Boolean {
            val listeners = android.provider.Settings.Secure.getString(
                context.contentResolver,
                "enabled_notification_listeners"
            )
            return listeners != null && listeners.contains(context.packageName)
        }
    }
}

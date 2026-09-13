package com.zinc.waver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zinc.waver.ui.presentation.HomeActivity
import com.zinc.waver.util.FcmTokenRegister
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var fcmTokenRegister: FcmTokenRegister

    companion object {
        private const val CHANNEL_ID = "waver_notification_channel"
        private const val CHANNEL_NAME = "Waver Notifications"
        private const val OPEN_ALARM_SCREEN_ACTION = "OPEN_ALARM_SCREEN"
        private const val NOTIFICATION_CLICK_ACTION_KEY = "notification_click_action"
        private const val NOTIFICATION_DEEP_LINK = "waver://alarm"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        fcmTokenRegister.register(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM_MESSAGE", "From: ${remoteMessage.from}")

        try {
            // Check if message contains a notification payload.
            remoteMessage.notification?.let {
                Log.d("FCM_MESSAGE", "Message Notification Body: ${it.body}")
                sendNotification(it.title ?: "", it.body ?: "")
            }

            // Check if message contains a data payload.
            if (remoteMessage.data.isNotEmpty()) {
                Log.d("FCM_MESSAGE", "Message data payload: ${remoteMessage.data}")
                val title = remoteMessage.data["title"]
                val body = remoteMessage.data["body"]
                if (title != null && body != null) {
                    sendNotification(title, body)
                }
            }
        } catch (e: Exception) {
            Log.e("FCM_MESSAGE", "Error processing message", e)
        }
    }

    private fun sendNotification(title: String, body: String) {
        try {
            val channelId = CHANNEL_ID
            val notificationId = System.currentTimeMillis().toInt()

            val intent = Intent(this, HomeActivity::class.java).apply {
                // 기존 HomeActivity가 있으면 재사용하고, 없으면 새 태스크로 연다.
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                action = Intent.ACTION_VIEW
                data = Uri.parse(NOTIFICATION_DEEP_LINK)
                putExtra("notification_title", title)
                putExtra("notification_body", body)
                putExtra(NOTIFICATION_CLICK_ACTION_KEY, OPEN_ALARM_SCREEN_ACTION)
            }

            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                this,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            val notificationManager = getSystemService(NotificationManager::class.java)

            // Android 8.0 이상에서는 알림 채널이 필요합니다.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Waver App Notifications"
                    enableVibration(true)
                    setShowBadge(true)
                }
                notificationManager.createNotificationChannel(channel)
            }

            // NotificationManagerCompat을 사용하여 권한 체크 없이 알림 전송
            NotificationManagerCompat.from(this).notify(notificationId, notificationBuilder.build())
            Log.d("FCM_MESSAGE", "Notification sent with ID: $notificationId")

        } catch (e: Exception) {
            Log.e("FCM_MESSAGE", "Error sending notification", e)
        }
    }
}

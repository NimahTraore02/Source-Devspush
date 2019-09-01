package com.decouikit.news.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.decouikit.news.R

class NotificationUtil {
    companion object {
        fun showNotification(context: Context, title: String?, description: String?) {
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val channelID = "1"
            val channelName = "News"

            val builder = NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setChannelId(channelID)
                .setContentText(description)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(defaultSoundUri)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel =
                    NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
                channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(0, builder.build())
        }

        fun cancelNotification(ctx: Context, notifyId: Int) {
            val ns = Context.NOTIFICATION_SERVICE
            val nMgr = ctx.getSystemService(ns) as NotificationManager
            nMgr.cancel(notifyId)
        }
    }
}

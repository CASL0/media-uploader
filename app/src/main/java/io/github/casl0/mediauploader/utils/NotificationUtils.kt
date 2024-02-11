package io.github.casl0.mediauploader.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

/**
 * 通知チャネルを作成します。
 *
 * @param channelId 通知チャネルのID
 * @param channelName 通知チャネル名
 * @param importance 通知のimportance
 */
internal fun NotificationManager.makeNotificationChannel(
    channelId: CharSequence,
    channelName: CharSequence,
    importance: Int,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            channelId.toString(),
            channelName,
            importance,
        )
        createNotificationChannel(notificationChannel)
    }
}

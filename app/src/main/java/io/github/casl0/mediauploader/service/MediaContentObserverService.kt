package io.github.casl0.mediauploader.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import io.github.casl0.mediauploader.R
import io.github.casl0.mediauploader.models.DomainUpdateHistory
import io.github.casl0.mediauploader.repository.UpdateHistoryRepository
import io.github.casl0.mediauploader.utils.OBSERVER_SERVICE_CHANNEL_ID
import io.github.casl0.mediauploader.utils.OBSERVER_SERVICE_CHANNEL_NAME
import io.github.casl0.mediauploader.utils.makeNotificationChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

/** [ContentObserver]を使ったメディアコンテンツの監視サービス */
@AndroidEntryPoint
class MediaContentObserverService(handler: Handler? = null) : Service(), IMediaMonitor {
    companion object {
        val IMAGES_URI: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val VIDEO_URI: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val AUDIO_URI: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }

    /** サービスのバインダー */
    private val binder = MediaContentObserverBinder()

    /** メディアコンテンツ監視用インスタンス */
    private val observer = object : ContentObserver(handler) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            Log.d(TAG, "onChange: $selfChange, $uri")

            if (uri != null) {
                coroutineScope.launch {
                    updateHistoryRepository.saveHistory(
                        DomainUpdateHistory(
                            uri = uri,
                            updatedAt = Clock.System.now(),
                        )
                    )
                }
            }
        }
    }

    /** 更新履歴のデータ */
    @Inject
    lateinit var updateHistoryRepository: UpdateHistoryRepository

    /** コルーチンスコープ */
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    //region Private Methods
    /**
     * 通知に表示する状態を更新します。
     *
     * @param message 通知に表示するメッセージ
     */
    private fun updateMonitoringStatus(@StringRes message: Int) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.makeNotificationChannel(
            OBSERVER_SERVICE_CHANNEL_ID,
            getString(OBSERVER_SERVICE_CHANNEL_NAME),
            NotificationManager.IMPORTANCE_LOW,
        )
        startForeground(
            NOTIFICATION_ID,
            NotificationCompat.Builder(this, OBSERVER_SERVICE_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText(getString(message))
                .build(),
        )
    }
    //endregion

    //region IMediaMonitor
    /** 監視を開始します。 */
    override fun start() {
        contentResolver.registerContentObserver(IMAGES_URI, true, observer)
        contentResolver.registerContentObserver(VIDEO_URI, true, observer)
        contentResolver.registerContentObserver(AUDIO_URI, true, observer)
        updateMonitoringStatus(R.string.monitor_enabled)
    }

    /** 監視を終了します。 */
    override fun stop() {
        contentResolver.unregisterContentObserver(observer)
        updateMonitoringStatus(R.string.monitor_disabled)
    }
    //endregion

    //region android.app.Service
    override fun onCreate() {
        super.onCreate()
        updateMonitoringStatus(R.string.monitor_disabled)
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
        coroutineScope.cancel()
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }
    //endregion

    //region Inner Classes
    /** サービスのバインダー */
    inner class MediaContentObserverBinder : Binder() {
        /** メディア監視用のインターフェース */
        val service: IMediaMonitor = this@MediaContentObserverService
    }
    //endregion
}

private const val TAG = "MediaContentObserverService"

// 通知は上書きするのでIDは固定
private const val NOTIFICATION_ID = 100

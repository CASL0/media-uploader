package io.github.casl0.mediauploader

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.casl0.mediauploader.service.IMediaMonitor
import io.github.casl0.mediauploader.service.MediaContentObserverService
import io.github.casl0.mediauploader.ui.MediaUploaderApp
import io.github.casl0.mediauploader.ui.theme.MediaUploaderTheme

class MainActivity : ComponentActivity() {
    private var mediaMonitorService: IMediaMonitor? = null

    //region android.app.Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MediaUploaderTheme {
                MediaUploaderApp()
            }
        }
        bindService()
    }
    //endregion

    //region Private Methods
    /** ファイル監視サービスをバインドします */
    private fun bindService() {
        Intent(this, MediaContentObserverService::class.java).also {
            bindService(
                it,
                object : ServiceConnection {
                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        Log.d(TAG, "onServiceConnected, ${name.toString()}")
                        val binder = service as MediaContentObserverService.MediaContentObserverBinder
                        mediaMonitorService = binder.service
                    }

                    override fun onServiceDisconnected(name: ComponentName?) {
                        Log.d(TAG, "onServiceDisconnected, ${name.toString()}")
                    }
                },
                BIND_AUTO_CREATE,
            )
        }
    }
    //endregion
}

private const val TAG = "MainActivity"

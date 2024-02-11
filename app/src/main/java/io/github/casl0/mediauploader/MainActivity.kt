package io.github.casl0.mediauploader

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import io.github.casl0.mediauploader.service.IMediaMonitor
import io.github.casl0.mediauploader.service.MediaContentObserverService
import io.github.casl0.mediauploader.ui.MediaUploaderApp
import io.github.casl0.mediauploader.ui.theme.MediaUploaderTheme
import io.github.casl0.mediauploader.utils.askPermissions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MainActivity : ComponentActivity() {
    companion object {
        /** メディアパーミッション要求コード */
        const val MEDIA_PERMISSION_REQUEST_CODE = 1000
    }

    private var mediaMonitorService: IMediaMonitor? = null

    /** UI状態（可変） */
    private val _uiState: MutableStateFlow<CommonUiState> = MutableStateFlow(CommonUiState())

    /** UI状態 */
    private val uiState: StateFlow<CommonUiState> = _uiState


    //region android.app.Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MediaUploaderTheme {
                MediaUploaderApp(uiState, this::rationaleAction)
            }
        }
        bindService()
        askMediaPermissions()
    }
    //endregion

    //region androidx.activity.ComponentActivity
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MEDIA_PERMISSION_REQUEST_CODE -> {
                if (
                    grantResults.any {
                        it == PackageManager.PERMISSION_DENIED
                    }
                ) {
                    Log.d(TAG, "メディアパーミッションが拒否されました。")
                }
            }
        }
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

    /** メディアパーミッションを要求します。 */
    private fun askMediaPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                android.Manifest.permission.READ_MEDIA_IMAGES,
                android.Manifest.permission.READ_MEDIA_VIDEO,
                android.Manifest.permission.READ_MEDIA_AUDIO,
            )
        } else {
            listOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }
        askPermissions(
            permissions, MEDIA_PERMISSION_REQUEST_CODE
        ) {
            _uiState.update {
                it.copy(
                    showSnackbar = true,
                    snackbarMessage = R.string.media_permission_rationale,
                    snackbarActionLabel = R.string.media_permission_rationale_label
                )
            }
        }
    }

    /**
     * パーミッション要求理由説明のアクション。
     *
     * アプリ情報の設定画面に遷移します。
     */
    private fun rationaleAction() {
        _uiState.update { it.copy(showSnackbar = false) }
        Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.parse("package:${packageName}")
        }.also {
            startActivity(it)
        }
    }
    //endregion
}

private const val TAG = "MainActivity"

/**
 * 共通のUI状態
 *
 * @property showSnackbar スナックバーを表示中
 * @property snackbarMessage スナックバーのメッセージ
 * @property snackbarActionLabel スナックバーのアクションラベル
 */
internal data class CommonUiState(
    val showSnackbar: Boolean = false,
    @StringRes val snackbarMessage: Int = 0,
    @StringRes val snackbarActionLabel: Int = 0,
)

package io.github.casl0.mediauploader

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import dagger.hilt.android.AndroidEntryPoint
import io.github.casl0.mediauploader.service.IMediaMonitor
import io.github.casl0.mediauploader.service.MediaContentObserverService
import io.github.casl0.mediauploader.ui.MediaUploaderApp
import io.github.casl0.mediauploader.ui.theme.MediaUploaderTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mediaPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        listOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO,
        )
    } else {
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    }

    private var mediaMonitorService: IMediaMonitor? = null

    /** UI状態（可変） */
    private val _uiState: MutableStateFlow<CommonUiState> = MutableStateFlow(CommonUiState())

    /** UI状態 */
    private val uiState: StateFlow<CommonUiState> = _uiState

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected, ${name.toString()}")
            val binder = service as MediaContentObserverService.MediaContentObserverBinder
            mediaMonitorService = binder.service
            if (uiState.value.observeEnabled) {
                mediaMonitorService?.start()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected, ${name.toString()}")
        }
    }


    //region android.app.Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MediaUploaderTheme {
                MediaUploaderApp(
                    uiState,
                    this::rationaleAction,
                    switchObserve = { enabled ->
                        if (enabled) {
                            mediaMonitorService?.start()

                        } else {
                            mediaMonitorService?.stop()
                        }
                        _uiState.update { it.copy(observeEnabled = enabled) }
                    },
                    openNotificationSetting = this::goToNotificationSetting,
                    mediaPermissions,
                    this::shouldShowRationale,
                    this::switchPermissionsRequested
                )
            }
        }
        bindService()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(ARG_UI_STATE, uiState.value)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedUiState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            savedInstanceState.getParcelable(ARG_UI_STATE, CommonUiState::class.java)
        } else {
            savedInstanceState.getParcelable(ARG_UI_STATE)
        }
        if (savedUiState != null) {
            _uiState.update { savedUiState }
        }
    }
    //endregion

    //region Private Methods
    /** ファイル監視サービスをバインドします */
    private fun bindService() {
        Intent(this, MediaContentObserverService::class.java).also {
            bindService(
                it,
                serviceConnection,
                BIND_AUTO_CREATE,
            )
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

    /** 通知の設定画面に遷移します。 */
    private fun goToNotificationSetting() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent().apply {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtras(bundleOf(Settings.EXTRA_APP_PACKAGE to packageName))
            }
        } else {
            Intent().apply {
                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                putExtras(
                    bundleOf(
                        "app_package" to packageName,
                        "app_uid" to applicationInfo.uid,
                    )
                )
            }
        }
        startActivity(intent)
    }

    /** パーミッションが必要な旨を表示する */
    private fun shouldShowRationale() {
        _uiState.update {
            it.copy(
                showSnackbar = true,
                snackbarMessage = R.string.media_permission_rationale,
                snackbarActionLabel = R.string.media_permission_rationale_label
            )
        }
    }

    /** パーミッション要求済みフラグ切り替え */
    private fun switchPermissionsRequested() {
        _uiState.update {
            it.copy(permissionsRequested = true)
        }
    }
    //endregion
}

private const val TAG = "MainActivity"

/** UI状態のSaved Stateのキー */
private const val ARG_UI_STATE = "ui_state"

/**
 * 共通のUI状態
 *
 * @property showSnackbar スナックバーを表示中
 * @property snackbarMessage スナックバーのメッセージ
 * @property snackbarActionLabel スナックバーのアクションラベル
 * @property observeEnabled 監視の有効・無効
 */
@Parcelize
internal data class CommonUiState(
    val showSnackbar: Boolean = false,
    @StringRes val snackbarMessage: Int = 0,
    @StringRes val snackbarActionLabel: Int = 0,
    val observeEnabled: Boolean = false,
    val permissionsRequested: Boolean = false,
) : Parcelable

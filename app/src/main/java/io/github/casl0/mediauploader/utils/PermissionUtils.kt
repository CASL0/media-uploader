package io.github.casl0.mediauploader.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * パーミッションを要求します。
 *
 * @param permissions 要求するパーミッションのリスト
 * @param requestCode パーミッションリクエストコード
 * @param showRationale パーミッション要求理由を表示する処理
 */
internal fun Activity.askPermissions(
    permissions: List<String>,
    requestCode: Int,
    showRationale: () -> Unit,
) {
    when {
        permissions.all {
            ContextCompat.checkSelfPermission(
                this, it
            ) == PackageManager.PERMISSION_GRANTED
        }    -> {
            // no-op
        }

        permissions.any {
            ActivityCompat.shouldShowRequestPermissionRationale(this, it)
        }    -> {
            showRationale()
        }

        else -> {
            requestPermissions(permissions.toTypedArray(), requestCode)
        }
    }
}

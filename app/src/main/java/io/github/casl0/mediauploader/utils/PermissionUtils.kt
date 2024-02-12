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
 * @return 既にパーミッションが付与されている場合はtrue、それ以外はfalse
 */
internal fun Activity.askPermissions(
    permissions: List<String>,
    requestCode: Int,
    showRationale: () -> Unit,
): Boolean {
    return when {
        permissions.all {
            ContextCompat.checkSelfPermission(
                this, it
            ) == PackageManager.PERMISSION_GRANTED
        }    -> {
            true
        }

        permissions.any {
            ActivityCompat.shouldShowRequestPermissionRationale(this, it)
        }    -> {
            showRationale()
            false
        }

        else -> {
            requestPermissions(permissions.toTypedArray(), requestCode)
            false
        }
    }
}

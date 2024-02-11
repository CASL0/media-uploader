package io.github.casl0.mediauploader.ui

import androidx.annotation.StringRes
import io.github.casl0.mediauploader.R

/**
 * ナビゲーション用のルート
 *
 * @property title AppBarに表示する文言
 */
internal enum class MediaUploaderRoute(
    @StringRes val title: Int,
) {
    Home(R.string.home_title),
    Settings(R.string.more_actions_settings),
}

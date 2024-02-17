package io.github.casl0.mediauploader.ui

import androidx.annotation.StringRes
import io.github.casl0.mediauploader.R

/**
 * ナビゲーション用のルート
 *
 * @property title AppBarに表示する文言
 * @property showAction AppBarにアクションを表示する
 */
internal enum class MediaUploaderRoute(
    @StringRes val title: Int,
    val showAction: Boolean,
) {
    Home(R.string.home_title, true),
    Settings(R.string.more_actions_settings, false),
}

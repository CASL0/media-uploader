package io.github.casl0.mediauploader.service

/** メディアコンテンツの監視用インターフェース */
interface IMediaMonitor {
    /** 監視を開始します。 */
    fun start()

    /** 監視を終了します。 */
    fun stop()
}

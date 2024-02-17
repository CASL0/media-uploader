package io.github.casl0.mediauploader.models

import android.net.Uri
import io.github.casl0.mediauploader.database.entities.UpdateHistory
import kotlinx.datetime.Instant

/**
 * 更新履歴のドメインモデル
 *
 * @property uri URI
 * @property updatedAt 更新日時
 */
data class DomainUpdateHistory(
    val uri: Uri,
    val updatedAt: Instant,
)

/**
 * 更新履歴のドメインモデルからデータベースエンティティへの変換
 *
 * @return データベースエンティティ
 */
internal fun DomainUpdateHistory.toDatabaseEntity(): UpdateHistory {
    return UpdateHistory(
        id = 0,
        uri = this.uri.toString(),
        updatedAt = this.updatedAt,
    )
}

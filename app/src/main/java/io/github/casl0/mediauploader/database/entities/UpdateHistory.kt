package io.github.casl0.mediauploader.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

/**
 * 更新履歴のエンティティ
 *
 * @property id ID
 * @property uri URI
 * @property updatedAt 更新日時
 */
@Entity("update_history")
data class UpdateHistory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "uri")
    val uri: String,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Instant,
)

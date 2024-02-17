package io.github.casl0.mediauploader.data.local

import android.net.Uri
import io.github.casl0.mediauploader.data.MediaUploaderDataSource
import io.github.casl0.mediauploader.database.MediaUploaderDatabase
import io.github.casl0.mediauploader.models.DomainUpdateHistory
import io.github.casl0.mediauploader.models.toDatabaseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** 更新履歴のローカルデータ */
class MediaUploaderLocalDataSource @Inject constructor(
    private val database: MediaUploaderDatabase,
) : MediaUploaderDataSource {
    /**
     * 更新履歴をすべて取得します
     *
     * @return 更新履歴のストリーム
     */
    override fun getAllHistory(): Flow<List<DomainUpdateHistory>> {
        return database.updateHistoryDao.getAll().map { entities ->
            entities.map {
                DomainUpdateHistory(
                    uri = Uri.parse(it.uri),
                    updatedAt = it.updatedAt,
                )
            }
        }
    }

    /**
     * 更新履歴を保存します
     *
     * @param newHistory 保存する更新履歴
     */
    override suspend fun saveHistory(newHistory: DomainUpdateHistory) {
        database.updateHistoryDao.save(newHistory.toDatabaseEntity())
    }
}

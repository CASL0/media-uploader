package io.github.casl0.mediauploader.repository

import io.github.casl0.mediauploader.data.MediaUploaderDataSource
import io.github.casl0.mediauploader.models.DomainUpdateHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** 更新履歴のデータ処理 */
class DefaultUpdateHistoryRepository @Inject constructor(
    private val updateHistoryDataSource: MediaUploaderDataSource,
) : UpdateHistoryRepository {
    /**
     * 更新履歴をすべて取得します
     *
     * @return 更新履歴のストリーム
     */
    override fun getAllHistory(): Flow<List<DomainUpdateHistory>> {
        return updateHistoryDataSource.getAllHistory()
    }

    /**
     * 更新履歴を保存します
     *
     * @param newHistory 保存する更新履歴
     */
    override suspend fun saveHistory(newHistory: DomainUpdateHistory) {
        updateHistoryDataSource.saveHistory(newHistory)
    }
}

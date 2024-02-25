package io.github.casl0.mediauploader.repository

import io.github.casl0.mediauploader.models.DomainUpdateHistory
import kotlinx.coroutines.flow.Flow

/** 更新履歴のデータ処理 */
interface UpdateHistoryRepository {
    /**
     * 更新履歴をすべて取得します
     *
     * @return 更新履歴のストリーム
     */
    fun getAllHistory(): Flow<List<DomainUpdateHistory>>

    /**
     * 更新履歴を保存します
     *
     * @param newHistory 保存する更新履歴
     */
    suspend fun saveHistory(newHistory: DomainUpdateHistory)
}

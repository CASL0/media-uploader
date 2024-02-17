package io.github.casl0.mediauploader.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.casl0.mediauploader.database.entities.UpdateHistory
import kotlinx.coroutines.flow.Flow

/** 更新履歴（[UpdateHistory]）のDAO */
@Dao
interface UpdateHistoryDao {
    /**
     * 最新順に全レコードを取得します
     *
     * @return 全レコードのストリーム
     */
    @Query("SELECT * FROM update_history ORDER BY updated_at DESC")
    fun getAll(): Flow<List<UpdateHistory>>

    /**
     * レコードを追加します
     *
     * @param updateHistory 追加するエンティティ
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(updateHistory: UpdateHistory)
}

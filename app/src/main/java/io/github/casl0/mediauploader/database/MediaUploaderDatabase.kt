package io.github.casl0.mediauploader.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.casl0.mediauploader.database.dao.UpdateHistoryDao
import io.github.casl0.mediauploader.database.entities.UpdateHistory
import io.github.casl0.mediauploader.database.utils.InstantConverter

/** アプリ内のデータベース */
@Database(
    entities = [UpdateHistory::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(InstantConverter::class)
abstract class MediaUploaderDatabase : RoomDatabase() {
    /** 更新履歴はこのDAOから操作します */
    abstract val updateHistoryDao: UpdateHistoryDao
}

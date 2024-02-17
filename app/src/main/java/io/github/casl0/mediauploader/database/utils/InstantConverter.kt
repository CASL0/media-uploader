package io.github.casl0.mediauploader.database.utils

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

/** [Instant]とエポック秒のコンバーター */
class InstantConverter {
    /**
     * [Instant] → エポック秒の変換
     *
     * @param dateTime [Instant]
     * @return エポック秒
     */
    @TypeConverter
    fun instantToEpoch(dateTime: Instant): Long {
        return dateTime.toEpochMilliseconds()
    }

    /**
     * エポック秒 → [Instant]の変換
     *
     * @param epoch エポック秒
     * @return [Instant]
     */
    @TypeConverter
    fun epochToInstant(epoch: Long): Instant {
        return Instant.fromEpochMilliseconds(epoch)
    }
}

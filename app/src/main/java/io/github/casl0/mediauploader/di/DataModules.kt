package io.github.casl0.mediauploader.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.casl0.mediauploader.database.MediaUploaderDatabase
import javax.inject.Singleton

/** データベース用のモジュール */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    /** [MediaUploaderDatabase]のDI */
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MediaUploaderDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MediaUploaderDatabase::class.java,
            "media_uploader.db"
        ).build()
    }
}

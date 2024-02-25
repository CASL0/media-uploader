package io.github.casl0.mediauploader.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.casl0.mediauploader.data.MediaUploaderDataSource
import io.github.casl0.mediauploader.data.local.MediaUploaderLocalDataSource
import io.github.casl0.mediauploader.database.MediaUploaderDatabase
import io.github.casl0.mediauploader.repository.DefaultUpdateHistoryRepository
import io.github.casl0.mediauploader.repository.UpdateHistoryRepository
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

/** データソースのDIモジュール */
@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    /** [MediaUploaderDataSource]のDI */
    @Singleton
    @Provides
    fun provideUpdateHistoryDataSource(database: MediaUploaderDatabase): MediaUploaderDataSource {
        return MediaUploaderLocalDataSource(database)
    }
}

/** リポジトリのDIモジュール */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    /** [UpdateHistoryRepository]のDI */
    @Singleton
    @Provides
    fun provideUpdateHistoryRepository(
        dataSource: MediaUploaderDataSource,
    ): UpdateHistoryRepository {
        return DefaultUpdateHistoryRepository(dataSource)
    }
}

package io.github.casl0.mediauploader.data.local

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import io.github.casl0.mediauploader.database.MediaUploaderDatabase
import io.github.casl0.mediauploader.models.DomainUpdateHistory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class MediaUploaderLocalDataSourceTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MediaUploaderDatabase

    private val data = DomainUpdateHistory(
        Uri.parse(""),
        Instant.fromEpochMilliseconds(1708169899305),
    )

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MediaUploaderDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun saveHistory_returnsGetAllHistoryStream() = runBlocking {
        val dataSource = MediaUploaderLocalDataSource(database)

        dataSource.saveHistory(data)
        assertThat(dataSource.getAllHistory().first(), `is`(listOf(data)))
    }
}

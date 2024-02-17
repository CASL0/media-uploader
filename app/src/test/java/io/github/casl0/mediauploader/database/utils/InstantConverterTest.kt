package io.github.casl0.mediauploader.database.utils

import kotlinx.datetime.Instant
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test

class InstantConverterTest {
    private val converter = InstantConverter()

    @Test
    fun instantToEpoch() {
        val instant = Instant.fromEpochMilliseconds(1708169899305)
        assertThat(converter.instantToEpoch(instant), `is`(1708169899305))
    }

    @Test
    fun epochToInstant() {
        val epoch = 1708169899305
        assertThat(converter.epochToInstant(epoch), `is`(Instant.fromEpochMilliseconds(epoch)))
    }
}

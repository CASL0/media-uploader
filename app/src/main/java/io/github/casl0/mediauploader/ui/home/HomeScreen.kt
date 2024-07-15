package io.github.casl0.mediauploader.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.casl0.mediauploader.models.DomainUpdateHistory

@Composable
internal fun HomeScreen(
    entries: List<DomainUpdateHistory>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        repeat(entries.size) {
            UpdateEntry(
                mediaUri = entries[it].uri.toString(),
                updatedAt = entries[it].updatedAt.toString(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

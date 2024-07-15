package io.github.casl0.mediauploader.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
internal fun UpdateEntry(
    mediaUri: CharSequence,
    updatedAt: CharSequence,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
    ) {
        UpdateEntryContent(
            mediaUri,
            updatedAt,
            Modifier
                .height(IntrinsicSize.Max)
                .padding(8.dp)
        )
    }
}

@Composable
private fun UpdateEntryContent(
    mediaUri: CharSequence,
    updatedAt: CharSequence,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = mediaUri,
            contentDescription = "Thumbnail",
            modifier = Modifier
                .size(48.dp, 48.dp)
                .clip(MaterialTheme.shapes.medium)
        )
        Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
            Text(text = mediaUri.toString())
            Text(text = updatedAt.toString())
        }
    }
}

@Preview
@Composable
private fun UpdateEntryPreview() {
    MaterialTheme {
        UpdateEntry(
            "content://media/external/images/media/1000000041",
            "2000-01-01T00:00:00Z"
        )
    }
}

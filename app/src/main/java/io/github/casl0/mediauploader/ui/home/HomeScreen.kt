package io.github.casl0.mediauploader.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState(HomeUiState())
    val updateHistory = uiState.updateHistory
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            updateHistory.forEach {
                UpdateEntry(
                    mediaUri = it.uri.toString(),
                    updatedAt = it.updatedAt.toString(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

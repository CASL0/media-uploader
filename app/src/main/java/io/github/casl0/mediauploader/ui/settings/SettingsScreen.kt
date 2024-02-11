package io.github.casl0.mediauploader.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.casl0.mediauploader.R

@Composable
internal fun SettingsScreen(
    observeEnabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClickMediaPermission: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        MediaObserverSwitch(
            checked = observeEnabled,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        TextButton(
            onClick = onClickMediaPermission,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.settings_media_permissions),
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MediaObserverSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.switch_monitor), modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen(false, {}, {})
    }
}

package io.github.casl0.mediauploader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.casl0.mediauploader.ui.MediaUploaderApp
import io.github.casl0.mediauploader.ui.theme.MediaUploaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MediaUploaderTheme {
                MediaUploaderApp()
            }
        }
    }
}

private const val TAG = "MainActivity"

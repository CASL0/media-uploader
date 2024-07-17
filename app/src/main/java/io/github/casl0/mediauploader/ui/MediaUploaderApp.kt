package io.github.casl0.mediauploader.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.casl0.mediauploader.CommonUiState
import io.github.casl0.mediauploader.R
import io.github.casl0.mediauploader.ui.home.HomeScreen
import io.github.casl0.mediauploader.ui.settings.SettingsScreen
import kotlinx.coroutines.flow.StateFlow

@Composable
internal fun MediaUploaderApp(
    uiState: StateFlow<CommonUiState>,
    onClickRationaleAction: () -> Unit,
    switchObserve: (Boolean) -> Unit,
    openNotificationSetting: () -> Unit,
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = MediaUploaderRoute.valueOf(
        backStackEntry?.destination?.route ?: MediaUploaderRoute.Home.name
    )
    val commonUiState by uiState.collectAsState()
    val context = LocalContext.current
    if (commonUiState.showSnackbar) {
        LaunchedEffect(snackbarHostState) {
            when (
                snackbarHostState.showSnackbar(
                    message = context.getString(commonUiState.snackbarMessage),
                    duration = SnackbarDuration.Long,
                    actionLabel = context.getString(commonUiState.snackbarActionLabel),
                )
            ) {
                SnackbarResult.ActionPerformed -> {
                    onClickRationaleAction()
                }

                SnackbarResult.Dismissed       -> {
                    // no-op
                }
            }
        }
    }

    Scaffold(
        topBar = {
            MediaUploaderAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                navigateTo = { route ->
                    navController.navigate(route)
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        NavHost(
            navController = navController,
            startDestination = MediaUploaderRoute.Home.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(it)
        ) {
            composable(route = MediaUploaderRoute.Home.name) {
                HomeScreen(
                    hiltViewModel()
                )
            }
            composable(route = MediaUploaderRoute.Settings.name) {
                SettingsScreen(
                    commonUiState.observeEnabled,
                    onCheckedChange = switchObserve,
                    onClickMediaPermission = onClickRationaleAction,
                    onClickNotificationPermission = openNotificationSetting
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediaUploaderAppBar(
    currentScreen: MediaUploaderRoute,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (currentScreen.showAction) {
                IconButton(onClick = { menuExpanded = !menuExpanded }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Show more"
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.more_actions_settings)) },
                        onClick = {
                            menuExpanded = false
                            navigateTo(MediaUploaderRoute.Settings.name)
                        },
                        modifier = Modifier.width(150.dp)
                    )
                }
            }
        },
    )
}

@Preview
@Composable
private fun MediaUploaderAppBarPreview() {
    MaterialTheme {
        MediaUploaderAppBar(
            currentScreen = MediaUploaderRoute.Home,
            canNavigateBack = true,
            navigateUp = {},
            navigateTo = {},
        )
    }
}

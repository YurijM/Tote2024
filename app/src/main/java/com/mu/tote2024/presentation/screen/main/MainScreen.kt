package com.mu.tote2024.presentation.screen.main

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_GAMBLER_IS_NOT_FOUND
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_PROFILE_IS_EMPTY
import com.mu.tote2024.data.utils.Constants.GAMBLER
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.ApplicationBar
import com.mu.tote2024.presentation.components.BottomNav
import com.mu.tote2024.presentation.navigation.NavGraphMain
import com.mu.tote2024.presentation.ui.common.UiState

/*@Preview(
    name = "Light",
    showBackground = true
)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun MainScreenPreview() {
    Tote2024Theme {
        MainScreen()
    }
}*/

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    toProfile: () -> Unit,
    toAuth: () -> Unit
) {
    val activity = (LocalContext.current as? Activity)

    val navMainController = rememberNavController()
    val navBackStackEntry by navMainController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isLoading = remember { mutableStateOf(false) }

    val state by viewModel.state.collectAsState()

    val result = state.result
    var data = GamblerModel()

    when (result) {
        is UiState.Loading -> {
            isLoading.value = true
        }

        is UiState.Success -> {
            isLoading.value = false
            data = result.data

            //viewModel.onEvent(MainEvent.OnGamblerChange)
        }

        is UiState.Error -> {
            isLoading.value = false
            when (result.message) {
                ERROR_PROFILE_IS_EMPTY -> toProfile()
                ERROR_GAMBLER_IS_NOT_FOUND -> toAuth()
            }

        }

        else -> {}
    }

    Scaffold(
        bottomBar = {
            if (GAMBLER.rate > 0) {
                BottomNav(currentRoute) { route ->
                    navMainController.navigate(route)
                }
            }
        },
        topBar = {
            ApplicationBar(
                navController = navMainController,
                photoUrl = data.profile.photoUrl,
                isAdmin = data.admin,
                onImageClick = { toProfile() },
                onSignOut = {
                    viewModel.signOut()
                    activity?.finish()
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
            contentColor = MaterialTheme.colorScheme.primary,
            color = MaterialTheme.colorScheme.surface
        ) {
            NavGraphMain(navMainController = navMainController)

            if (isLoading.value) {
                AppProgressBar()
            }
        }
    }
}

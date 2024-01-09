package com.mu.tote2024.presentation.screen.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_GAMBLER_IS_NOT_FOUND
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_PROFILE_IS_EMPTY
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.presentation.components.ApplicationBar
import com.mu.tote2024.presentation.components.BottomNav
import com.mu.tote2024.presentation.navigation.NavGraphMainScreen
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
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
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
            BottomNav(currentRoute) { route ->
                navController.navigate(route)
            }
        },
        topBar = {
            ApplicationBar(
                photoUrl = data.profile.photoUrl,
                isAdmin = data.admin,
                onImageClick = { toProfile() }
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
            NavGraphMainScreen(navController = navController)
        }
        if (isLoading.value) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

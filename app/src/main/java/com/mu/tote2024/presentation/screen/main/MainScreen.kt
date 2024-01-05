package com.mu.tote2024.presentation.screen.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_PROFILE_IS_EMPTY
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.presentation.components.ApplicationBar
import com.mu.tote2024.presentation.components.BottomNav
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.toLog

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
    toProfile: () -> Unit
) {
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
            toLog("UiState.Error: ${result.message}")
            if (result.message == ERROR_PROFILE_IS_EMPTY) toProfile()
        }

        else -> {}
    }

    Scaffold(
        bottomBar = {
            BottomNav()
        },
        topBar = {
            ApplicationBar(
                photoUrl = data.profile?.photoUrl ?: "",
                isAdmin = data.admin,
                onImageClick = { toProfile() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = 4.dp,
                    end = 4.dp,
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {

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

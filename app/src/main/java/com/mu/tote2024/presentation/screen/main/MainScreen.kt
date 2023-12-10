package com.mu.tote2024.presentation.screen.main

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.presentation.components.ApplicationBar
import com.mu.tote2024.presentation.components.BottomNav
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
    toProfile: () -> Unit
) {
    val gambler by viewModel.gambler.collectAsState()

    when (gambler.result) {
        is UiState.Success -> {
            viewModel.onEvent(MainEvent.OnGamblerChange)
        }

        else -> {}
    }

    Scaffold(
        bottomBar = {
            BottomNav()
        },
        topBar = {
            ApplicationBar(
                isAdmin = viewModel.isGamblerAdmin,
                onImageClick = { toProfile() }
            )
        }
    ) {
    }
}

package com.mu.tote2024.presentation.screen.main

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.presentation.components.ApplicationBar
import com.mu.tote2024.presentation.components.BottomNav
import com.mu.tote2024.presentation.ui.Tote2024Theme
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val gambler by viewModel.gambler.collectAsState()

    Scaffold(
        //containerColor = ColorBackgroundLight,
        bottomBar = {
            BottomNav()
        },
        topBar = {
            //ApplicationBar(isAdmin = viewModel.isAdmin)
            ApplicationBar(isAdmin = when (gambler.result) {
                is UiState.Success -> {
                    Log.d(Constants.DEBUG_TAG, "gambler: Success (${(gambler.result as UiState.Success).data})")
                    (gambler.result as UiState.Success).data.admin
                }
                else -> false
            })
        }
    ) {
    }
}

@Preview(
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
}

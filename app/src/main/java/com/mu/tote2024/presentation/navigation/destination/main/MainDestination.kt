package com.mu.tote2024.presentation.navigation.destination.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.main.MainScreen
import com.mu.tote2024.presentation.utils.Constants

fun NavGraphBuilder.main(
    toProfile: () -> Unit,
) {
    composable(Constants.Routes.MAIN_SCREEN) {
        MainScreen(
            toProfile = toProfile
        )
    }
}

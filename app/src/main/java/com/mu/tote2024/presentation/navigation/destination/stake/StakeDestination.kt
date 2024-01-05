package com.mu.tote2024.presentation.navigation.destination.stake

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.stake.StakeScreen
import com.mu.tote2024.presentation.utils.Constants.Routes.STAKES_SCREEN

fun NavGraphBuilder.stake() {
    composable(STAKES_SCREEN) {
        StakeScreen()
    }
}

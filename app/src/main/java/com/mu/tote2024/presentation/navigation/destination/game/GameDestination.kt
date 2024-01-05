package com.mu.tote2024.presentation.navigation.destination.game

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.game.GameScreen
import com.mu.tote2024.presentation.utils.Constants

fun NavGraphBuilder.game() {
    composable(Constants.Routes.GAMES_SCREEN) {
        GameScreen()
    }
}

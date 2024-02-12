package com.mu.tote2024.presentation.navigation.destination.game

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.game.GameScreen
import com.mu.tote2024.presentation.utils.Constants.KEY_ID
import com.mu.tote2024.presentation.utils.Constants.Routes.GAME_SCREEN

fun NavGraphBuilder.game() {
    composable(
        "$GAME_SCREEN/{$KEY_ID}"
    ) {
        GameScreen()
    }
}

fun NavController.navigateToGame(
    id: String
) {
    navigate("$GAME_SCREEN/$id")
}

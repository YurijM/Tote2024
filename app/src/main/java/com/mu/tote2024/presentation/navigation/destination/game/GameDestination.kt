package com.mu.tote2024.presentation.navigation.destination.game

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.game.list.GameListScreen
import com.mu.tote2024.presentation.utils.Constants

fun NavGraphBuilder.gameList() {
    composable(Constants.Routes.GAME_LIST_SCREEN) {
        GameListScreen()
    }
}

package com.mu.tote2024.presentation.navigation.destination.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.admin.game.AdminGameListScreen
import com.mu.tote2024.presentation.utils.Constants

fun NavGraphBuilder.adminGameList() {
    composable(Constants.Routes.ADMIN_GAME_LIST_SCREEN) {
        AdminGameListScreen()
    }
}


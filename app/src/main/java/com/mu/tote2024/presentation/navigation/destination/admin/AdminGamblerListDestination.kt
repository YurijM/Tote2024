package com.mu.tote2024.presentation.navigation.destination.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.admin.gambler.list.AdminGamblerListScreen
import com.mu.tote2024.presentation.utils.Constants.Routes.ADMIN_GAMBLER_LIST_SCREEN

fun NavGraphBuilder.adminGamblerList(
    toGambler: (String) -> Unit
) {
    composable(ADMIN_GAMBLER_LIST_SCREEN) {
        AdminGamblerListScreen(
            toGambler = toGambler
        )
    }
}

package com.mu.tote2024.presentation.navigation.destination.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.admin.gambler.AdminGamblerScreen
import com.mu.tote2024.presentation.utils.Constants.Routes.ADMIN_GAMBLER_SCREEN

fun NavGraphBuilder.adminGambler() {
    composable(ADMIN_GAMBLER_SCREEN) {
        AdminGamblerScreen()
    }
}

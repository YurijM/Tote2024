package com.mu.tote2024.presentation.navigation.destination.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.admin.main.AdminMainScreen
import com.mu.tote2024.presentation.utils.Constants.Routes.ADMIN_MAIN_SCREEN

fun NavGraphBuilder.adminMain(
    toItem: (String) -> Unit
) {
    composable(ADMIN_MAIN_SCREEN) {
        AdminMainScreen(
            toItem = toItem
        )
    }
}

package com.mu.tote2024.presentation.navigation.destination.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.admin.finish.AdminFinishScreen
import com.mu.tote2024.presentation.utils.Constants.Routes.ADMIN_FINISH_SCREEN

fun NavGraphBuilder.adminFinish() {
    composable(ADMIN_FINISH_SCREEN) {
        AdminFinishScreen()
    }
}


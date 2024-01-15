package com.mu.tote2024.presentation.navigation.destination.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.admin.email.list.AdminEmailListScreen
import com.mu.tote2024.presentation.utils.Constants.Routes.ADMIN_EMAIL_LIST_SCREEN

fun NavGraphBuilder.adminEmailList() {
    composable(ADMIN_EMAIL_LIST_SCREEN) {
        AdminEmailListScreen()
    }
}

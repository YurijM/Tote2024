package com.mu.tote2024.presentation.navigation.destination.admin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.admin.email.list.AdminEmailListScreen
import com.mu.tote2024.presentation.utils.Constants

fun NavGraphBuilder.adminEmailList(
    toEmail: (String) -> Unit
) {
    composable(Constants.Routes.ADMIN_EMAIL_LIST_SCREEN) {
        AdminEmailListScreen(
            toEmail = toEmail
        )
    }
}

fun NavController.navigateToAdminEmailList() {
    navigate(Constants.Routes.ADMIN_EMAIL_LIST_SCREEN) {
        popUpTo(Constants.Routes.ADMIN_EMAIL_SCREEN) {
            inclusive = true
        }
    }
}

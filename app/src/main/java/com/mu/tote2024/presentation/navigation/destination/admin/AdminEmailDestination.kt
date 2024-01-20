package com.mu.tote2024.presentation.navigation.destination.admin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.admin.email.AdminEmailScreen
import com.mu.tote2024.presentation.utils.Constants.KEY_ID
import com.mu.tote2024.presentation.utils.Constants.Routes.ADMIN_EMAIL_SCREEN

fun NavGraphBuilder.adminEmail(
    toAdminEmailList: () -> Unit
) {
    composable(
        "$ADMIN_EMAIL_SCREEN/{$KEY_ID}"
    ) {
        AdminEmailScreen(
            toAdminEmailList = toAdminEmailList
        )
    }
}

fun NavController.navigateToAdminEmail(
    id: String
) {
    navigate("$ADMIN_EMAIL_SCREEN/$id")
}

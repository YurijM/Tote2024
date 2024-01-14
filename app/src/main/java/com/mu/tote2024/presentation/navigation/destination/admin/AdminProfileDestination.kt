package com.mu.tote2024.presentation.navigation.destination.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.admin.profile.AdminProfileScreen
import com.mu.tote2024.presentation.utils.Constants.Routes.ADMIN_PROFILE_SCREEN

fun NavGraphBuilder.adminProfile() {
    composable(ADMIN_PROFILE_SCREEN) {
        AdminProfileScreen()
    }
}

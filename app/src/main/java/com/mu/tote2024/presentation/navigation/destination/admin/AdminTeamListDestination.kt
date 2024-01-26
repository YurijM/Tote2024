package com.mu.tote2024.presentation.navigation.destination.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.admin.team.AdminTeamListScreen
import com.mu.tote2024.presentation.utils.Constants

fun NavGraphBuilder.adminTeamList() {
    composable(Constants.Routes.ADMIN_TEAM_LIST_SCREEN) {
        AdminTeamListScreen()
    }
}


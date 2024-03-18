package com.mu.tote2024.presentation.navigation.destination.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.admin.stake.AdminStakeListScreen
import com.mu.tote2024.presentation.utils.Constants.Routes.ADMIN_STAKE_LIST_SCREEN

fun NavGraphBuilder.adminStakeList() {
    composable(ADMIN_STAKE_LIST_SCREEN) {
        AdminStakeListScreen()
    }
}


package com.mu.tote2024.presentation.navigation.destination.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.profile.ProfileScreen
import com.mu.tote2024.presentation.utils.Constants.Routes.PROFILE_SCREEN

fun NavGraphBuilder.profile(
    toMain: () -> Unit,
) {
    composable(PROFILE_SCREEN) {
        ProfileScreen(
            toMain = toMain
        )
    }
}

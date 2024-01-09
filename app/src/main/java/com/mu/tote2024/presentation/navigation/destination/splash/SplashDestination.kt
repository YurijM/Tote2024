package com.mu.tote2024.presentation.navigation.destination.splash

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.splash.SplashScreen
import com.mu.tote2024.presentation.utils.Constants

fun NavGraphBuilder.splash(
    toAuth: () -> Unit,
) {
    composable(Constants.Routes.SPLASH_SCREEN) {
        SplashScreen(
            toAuth = toAuth
        )
    }
}

package com.mu.tote2024.presentation.navigation.destination

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.splash.SplashScreen
import com.mu.tote2024.presentation.utils.Constants

fun NavGraphBuilder.splash(
    toSignUp: () -> Unit,
) {
    composable(Constants.Routes.SPLASH_SCREEN) {
        SplashScreen(
            toSignUp = toSignUp
        )
    }
}

fun NavController.navigateToAuth() {
    navigate(Constants.Routes.AUTH_SCREEN)
}
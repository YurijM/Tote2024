package com.mu.tote2024.presentation.navigation.destination

import androidx.navigation.NavController
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

fun NavController.navigateToAuth() {
    navigate(Constants.Routes.AUTH_SCREEN) {
        popUpTo(Constants.Routes.SPLASH_SCREEN) {
            //inclusive = true
        }
    }
}
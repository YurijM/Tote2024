package com.mu.tote2024.presentation.navigation.destination.splash

import androidx.navigation.NavController
import com.mu.tote2024.presentation.utils.Constants.Routes.AUTH_SCREEN
import com.mu.tote2024.presentation.utils.Constants.Routes.SPLASH_SCREEN

fun NavController.navigateToAuth() {
    navigate(AUTH_SCREEN) {
        popUpTo(SPLASH_SCREEN) {
            //inclusive = true
        }
    }
}
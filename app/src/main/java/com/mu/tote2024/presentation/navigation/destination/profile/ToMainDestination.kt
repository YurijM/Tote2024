package com.mu.tote2024.presentation.navigation.destination.profile

import androidx.navigation.NavController
import com.mu.tote2024.presentation.utils.Constants

fun NavController.navigateProfileToMain() {
    navigate(Constants.Routes.MAIN_SCREEN)
    {
        popUpTo(Constants.Routes.PROFILE_SCREEN) {
            inclusive = true
        }
    }
}
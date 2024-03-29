package com.mu.tote2024.presentation.navigation.destination.auth

import androidx.navigation.NavController
import com.mu.tote2024.presentation.utils.Constants.Routes.AUTH_SCREEN
import com.mu.tote2024.presentation.utils.Constants.Routes.MAIN_SCREEN

fun NavController.navigateToMain() {
    navigate(MAIN_SCREEN)
    {
        popUpTo(AUTH_SCREEN) /*{
            inclusive = true
        }*/
    }
}
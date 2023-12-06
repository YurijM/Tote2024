package com.mu.tote2024.presentation.navigation.destination.auth

import androidx.navigation.NavController
import com.mu.tote2024.presentation.utils.Constants

fun NavController.navigateToMain() {
    navigate(Constants.Routes.MAIN_SCREEN)
    {
        popUpTo(Constants.Routes.AUTH_SCREEN) /*{
            inclusive = true
        }*/
    }
}
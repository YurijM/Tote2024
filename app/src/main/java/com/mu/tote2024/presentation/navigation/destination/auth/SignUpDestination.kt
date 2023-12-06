package com.mu.tote2024.presentation.navigation.destination.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.auth.signup.SignUpScreen
import com.mu.tote2024.presentation.utils.Constants

fun NavGraphBuilder.signUp(
    toMain: () -> Unit
) {
    composable(Constants.Routes.SIGN_UP_SCREEN) {
        SignUpScreen(
            toMain = toMain
        )
    }
}

/*fun NavController.navigateToMain() {
    navigate(Constants.Routes.MAIN_SCREEN) {
        popUpTo(Constants.Routes.AUTH_SCREEN) *//*{
            inclusive = true
        }*//*
    }
}*/

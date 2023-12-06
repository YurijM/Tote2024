package com.mu.tote2024.presentation.navigation.destination.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.auth.signin.SignInScreen
import com.mu.tote2024.presentation.utils.Constants

fun NavGraphBuilder.signIn(
    toMain: () -> Unit
) {
    composable(Constants.Routes.SIGN_IN_SCREEN) {
        SignInScreen(
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

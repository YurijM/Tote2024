package com.mu.tote2024.presentation.navigation.destination

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.auth.AuthScreen
import com.mu.tote2024.presentation.utils.Constants

fun NavGraphBuilder.auth(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
) {
    composable(Constants.Routes.AUTH_SCREEN) {
        AuthScreen(
            onSignUpClick = onSignUpClick,
            onSignInClick = onSignInClick
        )
    }
}

fun NavController.navigateToSignUp() {
    navigate(Constants.Routes.SIGN_UP_SCREEN) {
        popUpTo(Constants.Routes.AUTH_SCREEN) /*{
            inclusive = true
        }*/
    }
}

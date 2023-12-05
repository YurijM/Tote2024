package com.mu.tote2024.presentation.navigation.destination

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.auth.signup.SignUpScreen
import com.mu.tote2024.presentation.utils.Constants

fun NavGraphBuilder.signUp(
    //onSignUpClick: () -> Unit
) {
    composable(Constants.Routes.SIGN_UP_SCREEN) {
        SignUpScreen(
            //onSignUpClick = onSignUpClick
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

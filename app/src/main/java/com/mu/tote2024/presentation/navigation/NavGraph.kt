package com.mu.tote2024.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mu.tote2024.presentation.navigation.destination.auth
import com.mu.tote2024.presentation.navigation.destination.navigateToAuth
import com.mu.tote2024.presentation.navigation.destination.navigateToSignUp
import com.mu.tote2024.presentation.navigation.destination.signUp
import com.mu.tote2024.presentation.navigation.destination.splash
import com.mu.tote2024.presentation.screen.auth.AuthScreen
import com.mu.tote2024.presentation.utils.Constants.Routes

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH_SCREEN
    ) {
        splash (
            toSignUp = {
                navController.navigateToAuth()
            }
        )

        auth(
            onSignUpClick = {
                navController.navigateToSignUp()
            },
            onSignInClick = {}
        )

        signUp (
            onSignUpClick = {}
        )
    }
}
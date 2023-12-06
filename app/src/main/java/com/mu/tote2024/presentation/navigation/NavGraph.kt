package com.mu.tote2024.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mu.tote2024.presentation.navigation.destination.auth.auth
import com.mu.tote2024.presentation.navigation.destination.main
import com.mu.tote2024.presentation.navigation.destination.navigateToAuth
import com.mu.tote2024.presentation.navigation.destination.auth.navigateToMain
import com.mu.tote2024.presentation.navigation.destination.auth.navigateToSignIn
import com.mu.tote2024.presentation.navigation.destination.auth.navigateToSignUp
import com.mu.tote2024.presentation.navigation.destination.auth.signIn
import com.mu.tote2024.presentation.navigation.destination.auth.signUp
import com.mu.tote2024.presentation.navigation.destination.splash
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
            onSignInClick = {
                navController.navigateToSignIn()
            }
        )

        signUp (
            toMain = {
                navController.navigateToMain()
            }
        )

        signIn (
            toMain = {
                navController.navigateToMain()
            }
        )

        main()
    }
}
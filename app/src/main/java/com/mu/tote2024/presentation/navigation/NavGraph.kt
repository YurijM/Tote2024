package com.mu.tote2024.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mu.tote2024.presentation.navigation.destination.auth.auth
import com.mu.tote2024.presentation.navigation.destination.auth.navigateToMain
import com.mu.tote2024.presentation.navigation.destination.auth.navigateToProfile
import com.mu.tote2024.presentation.navigation.destination.auth.navigateToSignIn
import com.mu.tote2024.presentation.navigation.destination.auth.navigateToSignUp
import com.mu.tote2024.presentation.navigation.destination.auth.signIn
import com.mu.tote2024.presentation.navigation.destination.auth.signUp
import com.mu.tote2024.presentation.navigation.destination.main.main
import com.mu.tote2024.presentation.navigation.destination.profile.navigateProfileToMain
import com.mu.tote2024.presentation.navigation.destination.profile.profile
import com.mu.tote2024.presentation.navigation.destination.splash.navigateToAuth
import com.mu.tote2024.presentation.navigation.destination.splash.splash
import com.mu.tote2024.presentation.utils.Constants.Routes

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH_SCREEN
    ) {
        splash(
            toAuth = {
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

        signUp(
            toProfile = {
                navController.navigateToProfile()
            }
        )

        signIn(
            toMain = {
                navController.navigateToMain()
            }
        )

        profile(
            toMain = {
                navController.navigateProfileToMain()
            },
            toAuth = {
                navController.navigateToAuth()
            }
        )

        main(
            toProfile = {
                navController.navigateToProfile()
            },
            toAuth = {
                navController.navigateToAuth()
            }
        )
    }
}
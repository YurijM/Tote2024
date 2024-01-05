package com.mu.tote2024.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.game.GameScreen
import com.mu.tote2024.presentation.screen.prognosis.PrognosisScreen
import com.mu.tote2024.presentation.screen.rating.RatingScreen
import com.mu.tote2024.presentation.screen.stake.StakeScreen
import com.mu.tote2024.presentation.utils.Constants.Routes.GAMES_SCREEN
import com.mu.tote2024.presentation.utils.Constants.Routes.PROGNOSIS_SCREEN
import com.mu.tote2024.presentation.utils.Constants.Routes.RATING_SCREEN
import com.mu.tote2024.presentation.utils.Constants.Routes.STAKES_SCREEN

@Composable
fun NavGraphMainScreen(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = RATING_SCREEN
    ) {
        composable(RATING_SCREEN) {
            RatingScreen()
        }
        composable(STAKES_SCREEN) {
            StakeScreen()
        }
        composable(PROGNOSIS_SCREEN) {
            PrognosisScreen()
        }
        composable(GAMES_SCREEN) {
            GameScreen()
        }
    }
}
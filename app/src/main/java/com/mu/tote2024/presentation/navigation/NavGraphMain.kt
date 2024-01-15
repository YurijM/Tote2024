package com.mu.tote2024.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mu.tote2024.presentation.navigation.destination.admin.adminEmailList
import com.mu.tote2024.presentation.navigation.destination.admin.adminMain
import com.mu.tote2024.presentation.navigation.destination.admin.adminGambler
import com.mu.tote2024.presentation.navigation.destination.admin.adminGamblerList
import com.mu.tote2024.presentation.navigation.destination.game.game
import com.mu.tote2024.presentation.navigation.destination.prognosis.prognosis
import com.mu.tote2024.presentation.navigation.destination.rating.rating
import com.mu.tote2024.presentation.navigation.destination.stake.stake
import com.mu.tote2024.presentation.utils.Constants.Routes.RATING_SCREEN

@Composable
fun NavGraphMain(
    navMainController: NavHostController
) {
    NavHost(
        navController = navMainController,
        startDestination = RATING_SCREEN
    ) {
        rating()
        stake()
        prognosis()
        game()

        adminMain(
            toItem = { route ->
                navMainController.navigate(route)
            }
        )

        adminEmailList()

        adminGamblerList()

        adminGambler()
    }
}
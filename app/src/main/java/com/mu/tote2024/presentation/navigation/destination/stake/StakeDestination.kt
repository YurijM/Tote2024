package com.mu.tote2024.presentation.navigation.destination.stake

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.stake.StakeScreen
import com.mu.tote2024.presentation.utils.Constants.KEY_ID
import com.mu.tote2024.presentation.utils.Constants.Routes.STAKE_SCREEN

fun NavGraphBuilder.stake(
    toStakeList: () -> Unit
) {
    composable("$STAKE_SCREEN/{$KEY_ID}") {
        StakeScreen(
            toStakeList = toStakeList
        )
    }
}

fun NavController.navigateToStake(
    id: String
) {
    navigate("$STAKE_SCREEN/$id")
}

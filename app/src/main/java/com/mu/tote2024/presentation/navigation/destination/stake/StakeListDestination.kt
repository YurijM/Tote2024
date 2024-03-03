package com.mu.tote2024.presentation.navigation.destination.stake

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.stake.list.StakeListScreen
import com.mu.tote2024.presentation.utils.Constants.Routes.STAKE_LIST_SCREEN

fun NavGraphBuilder.stakeList(
    toStake: (String) -> Unit
) {
    composable(
        STAKE_LIST_SCREEN
    ) {
        StakeListScreen(
            toStake = toStake
        )
    }
}

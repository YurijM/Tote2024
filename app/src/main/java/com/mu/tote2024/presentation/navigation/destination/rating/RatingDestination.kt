package com.mu.tote2024.presentation.navigation.destination.rating

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.rating.RatingScreen
import com.mu.tote2024.presentation.utils.Constants.Routes.RATING_SCREEN

fun NavGraphBuilder.rating() {
    composable(RATING_SCREEN) {
        RatingScreen()
    }
}

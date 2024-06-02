package com.mu.tote2024.presentation.navigation.destination.rating

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.rating.chart.RatingChartScreen
import com.mu.tote2024.presentation.utils.Constants.KEY_ID
import com.mu.tote2024.presentation.utils.Constants.Routes.RATING_CHART_SCREEN

fun NavGraphBuilder.ratingChart() {
    composable(
        "$RATING_CHART_SCREEN/{$KEY_ID}"
    ) {
        RatingChartScreen()
    }
}

fun NavController.navigateToRatingChart(id: String) {
    navigate("$RATING_CHART_SCREEN/$id")
}

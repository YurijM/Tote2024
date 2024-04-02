package com.mu.tote2024.presentation.navigation.destination.prognosis

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.prognosis.PrognosisListScreen
import com.mu.tote2024.presentation.utils.Constants.Routes.PROGNOSIS_SCREEN

fun NavGraphBuilder.prognosis() {
    composable(PROGNOSIS_SCREEN) {
        PrognosisListScreen()
    }
}

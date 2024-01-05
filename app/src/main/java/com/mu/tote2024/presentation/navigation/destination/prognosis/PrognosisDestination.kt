package com.mu.tote2024.presentation.navigation.destination.prognosis

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.prognosis.PrognosisScreen
import com.mu.tote2024.presentation.utils.Constants

fun NavGraphBuilder.prognosis() {
    composable(Constants.Routes.PROGNOSIS_SCREEN) {
        PrognosisScreen()
    }
}

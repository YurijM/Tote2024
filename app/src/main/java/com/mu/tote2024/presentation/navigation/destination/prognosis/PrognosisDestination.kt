package com.mu.tote2024.presentation.navigation.destination.prognosis

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.prognosis.PrognosisScreen
import com.mu.tote2024.presentation.utils.Constants.Routes.PROGNOSIS_SCREEN

fun NavGraphBuilder.prognosis(
    toPrognosisList: () -> Unit
) {
    composable(PROGNOSIS_SCREEN) {
        PrognosisScreen(
            toPrognosisList = toPrognosisList
        )
    }
}

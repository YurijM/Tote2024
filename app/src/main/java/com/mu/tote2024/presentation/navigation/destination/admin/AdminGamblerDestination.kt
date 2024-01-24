package com.mu.tote2024.presentation.navigation.destination.admin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mu.tote2024.presentation.screen.admin.gambler.AdminGamblerScreen
import com.mu.tote2024.presentation.utils.Constants.KEY_ID
import com.mu.tote2024.presentation.utils.Constants.Routes.ADMIN_GAMBLER_SCREEN

fun NavGraphBuilder.adminGambler(
    toAdminGamblerList: () -> Unit,
    toAdminGamblerPhoto: (String) -> Unit
) {
    composable(
        "$ADMIN_GAMBLER_SCREEN/{$KEY_ID}",
        arguments = listOf(
            navArgument(KEY_ID) {
                type = NavType.StringType
                nullable = false
            }
        )
    ) {
        AdminGamblerScreen(
            toAdminGamblerList = toAdminGamblerList,
            toAdminGamblerPhoto = toAdminGamblerPhoto
        )
    }
}

fun NavController.navigateToAdminGambler(
    id: String
) {
    navigate("$ADMIN_GAMBLER_SCREEN/$id")
}

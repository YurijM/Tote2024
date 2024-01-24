package com.mu.tote2024.presentation.navigation.destination.admin

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mu.tote2024.presentation.screen.admin.gambler.AdminGamblerPhotoScreen
import com.mu.tote2024.presentation.utils.Constants.KEY_PHOTO_URL
import com.mu.tote2024.presentation.utils.Constants.Routes.ADMIN_GAMBLER_PHOTO_SCREEN

fun NavGraphBuilder.adminGamblerPhoto() {
    composable(
        "$ADMIN_GAMBLER_PHOTO_SCREEN/{$KEY_PHOTO_URL}",
        arguments = listOf(
            navArgument(KEY_PHOTO_URL) {
                type = NavType.StringType
                nullable = false
            }
        )
    ) { navBackStackEntry ->
        val arguments = requireNotNull(navBackStackEntry.arguments)
        val photoUrl = arguments.getString(KEY_PHOTO_URL).orEmpty()

        AdminGamblerPhotoScreen(photoUrl)
    }
}

fun NavController.navigateToAdminGamblerPhoto(
    photoUrl: String
) {
    val encodedPhotoUrl = Uri.encode(photoUrl)
    navigate("$ADMIN_GAMBLER_PHOTO_SCREEN/$encodedPhotoUrl")
}

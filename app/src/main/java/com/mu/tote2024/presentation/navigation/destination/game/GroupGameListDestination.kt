package com.mu.tote2024.presentation.navigation.destination.game

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.tote2024.presentation.screen.game.list_group.GroupGameListScreen
import com.mu.tote2024.presentation.utils.Constants.KEY_GROUP
import com.mu.tote2024.presentation.utils.Constants.Routes.GROUP_GAME_LIST_SCREEN

fun NavGraphBuilder.groupGameList() {
    composable(
        "$GROUP_GAME_LIST_SCREEN/{$KEY_GROUP}"
    ) {
        GroupGameListScreen()
    }
}

fun NavController.navigateToGroupGameList(
    group: String
) {
    navigate("$GROUP_GAME_LIST_SCREEN/$group")
}

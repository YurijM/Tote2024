package com.mu.tote2024.presentation.screen.admin.main

import androidx.annotation.StringRes
import com.mu.tote2024.R
import com.mu.tote2024.presentation.utils.Constants

sealed class AdminNavItem(
    @StringRes val itemId: Int,
    val route: String
) {
    object AdminEmailItem: AdminNavItem(
        itemId = R.string.admin_email_list,
        route = Constants.Routes.ADMIN_EMAIL_LIST_SCREEN
    )
    object AdminGamblerItem: AdminNavItem(
        itemId = R.string.admin_gambler_list,
        route = Constants.Routes.ADMIN_GAMBLER_LIST_SCREEN
    )
    object AdminTeamItem: AdminNavItem(
        itemId = R.string.admin_team_list,
        route = Constants.Routes.ADMIN_TEAM_LIST_SCREEN
    )
    object AdminGameItem: AdminNavItem(
        itemId = R.string.admin_game_list,
        route = Constants.Routes.ADMIN_GAME_LIST_SCREEN
    )
}

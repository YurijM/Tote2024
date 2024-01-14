package com.mu.tote2024.presentation.screen.admin.main

import androidx.annotation.StringRes
import com.mu.tote2024.R
import com.mu.tote2024.presentation.utils.Constants

sealed class AdminNavItem(
    @StringRes val itemId: Int,
    val route: String
) {
    object AdminProfileItem: AdminNavItem(
        itemId = R.string.profile_admin,
        route = Constants.Routes.ADMIN_PROFILE_SCREEN
    )
}

package com.mu.tote2024.presentation.screen.admin.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminMainScreen(
    toItem: (String) -> Unit
) {
    val navItems = listOf(
        AdminNavItem.AdminEmailItem,
        AdminNavItem.AdminGamblerItem,
        AdminNavItem.AdminTeamItem,
        AdminNavItem.AdminGameItem,
        AdminNavItem.AdminStakeItem,
        AdminNavItem.AdminFinishItem,
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = 8.dp,
                horizontal = 24.dp
            )
    ) {
        LazyColumn {
            items(navItems) { item ->
                AdminItemScreen(item) { route ->
                    toItem(route)
                }
            }
        }
    }

}
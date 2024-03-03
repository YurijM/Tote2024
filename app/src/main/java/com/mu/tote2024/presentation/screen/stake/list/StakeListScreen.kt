package com.mu.tote2024.presentation.screen.stake.list

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

@Composable
fun StakeListScreen(
    toStake: (String) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Список игр для ставок",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
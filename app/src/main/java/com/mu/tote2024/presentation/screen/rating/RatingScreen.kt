package com.mu.tote2024.presentation.screen.rating

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.mu.tote2024.R

@Composable
fun RatingScreen() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.rating),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
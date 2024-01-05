package com.mu.tote2024.presentation.screen.prognosis

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.mu.tote2024.R

@Composable
fun PrognosisScreen() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.prognosis),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
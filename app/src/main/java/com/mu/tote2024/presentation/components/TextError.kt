package com.mu.tote2024.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TextError(
    errorMessage: String,
    textAlign: TextAlign? = null,
) {
    Text(
        modifier = Modifier.padding(
            top = 4.dp,
            start = 4.dp,
            end = 4.dp,
            bottom = 0.dp
        ),
        text = errorMessage,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.labelSmall,
        textAlign = textAlign
    )
}
package com.mu.tote2024.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        text = errorMessage,
        color = MaterialTheme.colorScheme.error,
        //style = MaterialTheme.typography.labelLarge,
        textAlign = textAlign
    )
}
package com.mu.tote2024.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AppTournamentIsFinished(
    text: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        border = BorderStroke(
            width = 2.dp,
            color = Color.White
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = Color.White
        )
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2
        )
    }
}
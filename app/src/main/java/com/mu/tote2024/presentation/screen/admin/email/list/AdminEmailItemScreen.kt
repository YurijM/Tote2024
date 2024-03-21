package com.mu.tote2024.presentation.screen.admin.email.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mu.tote2024.domain.model.EmailModel

@Composable
fun AdminEmailItemScreen(
    email: EmailModel,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = email.email,
            color = MaterialTheme.colorScheme.onSurface
        )
        IconButton(
            onClick = { onDelete() }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "deleteEmail"
            )
        }
    }
}
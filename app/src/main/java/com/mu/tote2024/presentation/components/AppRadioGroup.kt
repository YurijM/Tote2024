package com.mu.tote2024.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun AppRadioGroup(
    modifier: Modifier = Modifier,
    items: List<String>,
    currentValue: String,
    onClick: (newValue: String) -> Unit,
    errorMessage: String?
) {
    Card(
        modifier = modifier,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        ),
    ) {
        Column(
            modifier = Modifier
                .selectableGroup()
                .padding(8.dp)
        ) {
            items.forEach { item ->
                Row(
                    Modifier
                        .selectable(
                            selected = (item == currentValue),
                            onClick = { onClick(item) },
                            role = Role.RadioButton
                        ),
                        //.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (item == currentValue),
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = item,
                        modifier = Modifier.padding(start = 12.dp),
                        //style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
    if (!errorMessage.isNullOrBlank()) {
        TextError(errorMessage)
    }
}

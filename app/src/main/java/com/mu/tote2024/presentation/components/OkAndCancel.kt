package com.mu.tote2024.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mu.tote2024.R

@Composable
fun OkAndCancel(
    @StringRes titleOk: Int = R.string.ok,
    enabledOk: Boolean = false,
    showCancel: Boolean = true,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Spacer(modifier = Modifier.weight(.1f))
        Button(
            modifier = if (showCancel) {
                Modifier.weight(1f)
            } else {
                Modifier.wrapContentWidth()
            },
            enabled = enabledOk,
            onClick = { onSave() }
        ) {
            Text(
                text = stringResource(id = titleOk),
                style = MaterialTheme.typography.titleMedium
            )
        }
        if (showCancel) {
            Spacer(modifier = Modifier.weight(.1f))
            Button(
                modifier = Modifier.weight(1f),
                onClick = { onCancel() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Spacer(modifier = Modifier.weight(.1f))
    }
}
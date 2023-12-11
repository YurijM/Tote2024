package com.mu.tote2024.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/*@Preview(
    name = "Light",
    showBackground = true
)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun PreviewAppField() {
    Tote2024Theme {
        AppTextField(
            label = "email",
            value = "",
            onChange = {},
            painterId = R.drawable.ic_mail,
            description = "email",
            errorMessage = stringResource(R.string.error_field_empty)
        )
    }
}*/

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    label: String,
    value: String?,
    onChange: (newValue: String) -> Unit,
    errorMessage: String?
) {
    Column {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value ?: "",
            shape = ShapeDefaults.Medium,
            onValueChange = { newValue ->
                onChange(newValue)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                errorLeadingIconColor = MaterialTheme.colorScheme.error
            ),
            label = {
                Text(text = label)
            },
            singleLine = true,
            isError = !errorMessage.isNullOrBlank()
        )
        if (!errorMessage.isNullOrBlank()) {
            TextError(errorMessage)
        }
    }
}

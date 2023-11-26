package com.mu.tote2024.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    label: String,
    value: String?,
    onChange: (newValue: String) -> Unit,
    @DrawableRes painterId: Int,
    description: String,
    errorMessage: String
) {
    val hasError = remember {
        mutableStateOf(false)
    }

    Column {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value ?: "",
            shape = ShapeDefaults.Medium,
            onValueChange = { newValue ->
                hasError.value = newValue.isBlank()
                onChange(newValue)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                errorLeadingIconColor = MaterialTheme.colorScheme.error
            ),
            label = {
                Text(text = label)
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(id = painterId),
                    contentDescription = description
                )
            },
            singleLine = true,
            isError = hasError.value
        )
        if (hasError.value) {
            TextError(errorMessage)
        }
    }
}

package com.mu.tote2024.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
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
fun PreviewAppFieldWithIcon() {
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

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    label: String,
    textAlign: TextAlign = TextAlign.Unspecified,
    value: String?,
    onChange: (newValue: String) -> Unit,
    @DrawableRes painterId: Int? = null,
    description: String? = null,
    errorMessage: String?,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            value = value ?: "",
            shape = ShapeDefaults.Medium,
            onValueChange = { newValue ->
                onChange(newValue)
            },
            textStyle = TextStyle(
                textAlign = textAlign
            ),
            colors = OutlinedTextFieldDefaults.colors(
                errorLeadingIconColor = MaterialTheme.colorScheme.error,
            ),
            //textStyle = MaterialTheme.typography.bodyLarge,
            label = {
                if (label.isNotBlank()) {
                    Text(
                        text = label,
                        //style = MaterialTheme.typography.labelLarge
                    )
                }
            },
            leadingIcon = if (painterId != null) {
                {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(id = painterId),
                        contentDescription = description
                    )
                }
            } else null,
            singleLine = true,
            isError = !errorMessage.isNullOrBlank()
        )
        if (!errorMessage.isNullOrBlank()) {
            TextError(errorMessage)
        }
    }
}

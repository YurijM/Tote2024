package com.mu.tote2024.presentation.components

import android.annotation.SuppressLint
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

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    label: String,
    value: String?,
    onChange: (newValue: String) -> Unit,
    @DrawableRes painterId: Int? = null,
    description: String? = null,
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

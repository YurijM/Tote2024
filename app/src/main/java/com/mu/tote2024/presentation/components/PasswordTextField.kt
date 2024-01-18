package com.mu.tote2024.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mu.tote2024.R

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
fun PreviewPasswordField() {
    Tote2024Theme {
        PasswordTextField(
            label = "password",
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
fun PasswordTextField(
    label: String,
    value: String?,
    onChange: (newValue: String) -> Unit,
    @DrawableRes painterId: Int,
    description: String,
    errorMessage: String?
) {
    var showPassword by remember {
        mutableStateOf(false)
    }

    Column {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value ?: "",
            shape = ShapeDefaults.Medium,
            onValueChange = { newValue ->
                onChange(newValue)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
            ),
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
            visualTransformation = if (showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            showPassword = !showPassword
                        },
                    painter = if (showPassword) {
                        painterResource(id = R.drawable.ic_hide)
                    } else {
                        painterResource(id = R.drawable.ic_show)
                    },
                    contentDescription = "eye"
                )
            },
            isError = !errorMessage.isNullOrBlank()
        )
        if (!errorMessage.isNullOrBlank()) {
            TextError(errorMessage)
        }
    }
}

package com.mu.tote2024.presentation.screen.auth.signin

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.presentation.components.AdminTitle
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.AppTextField
import com.mu.tote2024.presentation.components.PasswordTextField
import com.mu.tote2024.presentation.components.TextError
import com.mu.tote2024.presentation.ui.common.UiState

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    toMain: () -> Unit
) {
    val isLoading = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf("") }

    val state by viewModel.state.collectAsState()

    when (state.result) {
        is UiState.Loading -> {
            isLoading.value = true
            error.value = ""
        }

        is UiState.Success -> {
            isLoading.value = false
            error.value = ""
            toMain()
        }

        is UiState.Error -> {
            isLoading.value = false
            error.value = (state.result as UiState.Error).message
        }

        else -> {}
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            /*Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.sign_in),
                style = MaterialTheme.typography.titleLarge
            )*/
            AdminTitle(title = R.string.sign_in)
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 12.dp,
                            start = 12.dp,
                            end = 12.dp,
                            bottom = 16.dp,
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    /*Column {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = viewModel.mail.value,
                            shape = ShapeDefaults.Medium,
                            onValueChange = { newValue ->
                                viewModel.onEvent(SignInEvent.OnEmailChange(newValue))
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                errorLeadingIconColor = MaterialTheme.colorScheme.error
                            ),
                            label = {
                                Text(text = stringResource(id = R.string.enter_email))
                            },
                            leadingIcon = {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        painter = painterResource(id = R.drawable.ic_mail),
                                        contentDescription = "email"
                                    )
                                },
                            singleLine = true,
                            isError = !viewModel.signInFields.errorEmail.isNullOrBlank()
                        )
                        *//*if (!viewModel.signInFields.errorEmail.isNullOrBlank()) {
                            TextError(viewModel.signInFields.errorEmail!!)
                        }*//*
                    }*/
                    AppTextField(
                        value = viewModel.signInFields.email,
                        onChange = { newValue ->
                            viewModel.onEvent(SignInEvent.OnEmailChange(newValue))
                        },
                        label = stringResource(id = R.string.enter_email),
                        painterId = R.drawable.ic_mail,
                        description = "email",
                        errorMessage = viewModel.signInFields.errorEmail,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    PasswordTextField(
                        label = stringResource(id = R.string.enter_password),
                        value = viewModel.signInFields.password,
                        onChange = { newValue ->
                            viewModel.onEvent(SignInEvent.OnPasswordChange(newValue))
                        },
                        painterId = R.drawable.ic_password,
                        description = "password",
                        errorMessage = viewModel.signInFields.errorPassword
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        enabled = viewModel.enabledButton,
                        onClick = {
                            viewModel.onEvent(
                                SignInEvent.OnSignIn(
                                    email = viewModel.signInFields.email,
                                    password = viewModel.signInFields.password
                                )
                            )
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.to_log_into),
                            //style = MaterialTheme.typography.titleMedium
                        )
                    }
                    if (error.value.isNotBlank()) {
                        TextError(
                            errorMessage = error.value,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        if (isLoading.value) {
            AppProgressBar()
        }
    }
}
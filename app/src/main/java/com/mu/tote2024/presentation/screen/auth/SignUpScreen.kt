package com.mu.tote2024.presentation.screen.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.presentation.components.AppTextField
import com.mu.tote2024.presentation.components.PasswordTextField
import com.mu.tote2024.presentation.components.TextError
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.DEBUG_TAG

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
fun SignUpScreenPreview() {
    Tote2024Theme {
        SignUpScreen(
            onSignUpClick = {}
        )
    }
}*/

@SuppressLint("UnrememberedMutableState")
@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    //var email by mutableStateOf<String?>(null)
    //var password by mutableStateOf<String?>(null)
    //var passwordConfirm by mutableStateOf<String?>(null)

    //var errorEmail by mutableStateOf<String?>(null)
    //var errorPassword by mutableStateOf<String?>(null)
    //var errorPasswordConfirm by mutableStateOf<String?>(null)

    val state by viewModel.state.collectAsState()

    Log.d(DEBUG_TAG, "start state: $state")

    /*LaunchedEffect(key1 = true) {
        if (email.value != null && email.value!!.isNotBlank()
            && password.value != null && password.value!!.isNotBlank()
        ) {
            when {
                state.isLoading -> {
                    Log.d(DEBUG_TAG, "state.isLoading")
                    isLoading.value = true
                }

                state.isSuccess -> {
                    //isLoading.value = false
                    Log.d(DEBUG_TAG, "state.isSuccess")

                    viewModel.sendEvent(AuthEvent.Default)

                    navController.navigate(Routes.MAIN_SCREEN) {
                        popUpTo(Routes.LOGON_SCREEN) {
                            inclusive = true
                        }
                    }
                }

                (state.error != null) -> {
                    //isLoading.value = false
                    Log.d(DEBUG_TAG, "state.error")
                }

                else -> {}
            }
        }
    }*/

    /*when {
        state.isLoading -> {
            Log.d(DEBUG_TAG, "state.isLoading")
            isLoading.value = true
        }

        state.isSuccess -> {
            Log.d(DEBUG_TAG, "state.isSuccess")
            isLoading.value = false

            viewModel.sendEvent(AuthEvent.Default)

            navController.navigate(Routes.MAIN_SCREEN) {
                popUpTo(Routes.LOGON_SCREEN) {
                    inclusive = true
                }
            }
        }

        (state.error != null) -> {
            Log.d(DEBUG_TAG, "state.error")
            isLoading.value = false
        }

        else -> {*/
    when (state.result) {
        is UiState.Default -> Log.d(DEBUG_TAG, "state: Default")
        is UiState.Loading -> Log.d(DEBUG_TAG, "state: Loading")
        is UiState.Success -> {
            Log.d(DEBUG_TAG, "state: Success (${(state.result as UiState.Success<Boolean>).data})")
        }
        is UiState.Error -> {
            Log.d(DEBUG_TAG, "state: Error (${(state.result as UiState.Error).message})")
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.sign_up),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
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
                    AppTextField(
                        value = viewModel.email,
                        onChange = { newValue ->
                            viewModel.onEvent(AuthEvent.OnEmailChange(newValue))
                        },
                        label = stringResource(id = R.string.enter_email),
                        painterId = R.drawable.ic_mail,
                        description = "email",
                        errorMessage = viewModel.errorEmail
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    PasswordTextField(
                        label = stringResource(id = R.string.enter_password),
                        value = viewModel.password,
                        onChange = { newValue ->
                            viewModel.onEvent(AuthEvent.OnPasswordChange(newValue))
                        },
                        painterId = R.drawable.ic_password,
                        description = "password",
                        errorMessage = viewModel.errorPassword
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    PasswordTextField(
                        label = stringResource(id = R.string.confirm_password),
                        value = viewModel.passwordConfirm,
                        onChange = { newValue ->
                            viewModel.onEvent(AuthEvent.OnPasswordConfirmChange(newValue))
                        },
                        painterId = R.drawable.ic_password,
                        description = "passwordConfirm",
                        errorMessage = viewModel.errorPasswordConfirm
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        enabled = (viewModel.errorEmail != null && viewModel.errorEmail!!.isBlank()) &&
                                (viewModel.errorPassword != null && viewModel.errorPassword!!.isBlank()) &&
                                (viewModel.errorPasswordConfirm != null && viewModel.errorPasswordConfirm!!.isBlank()),
                        onClick = {
                            viewModel.onEvent(AuthEvent.OnSignUp(
                                email = viewModel.email,
                                password = viewModel.password
                            ))
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.to_register)
                        )
                    }
                    if (state.result is UiState.Error) {
                        TextError(
                            errorMessage = (state.result as UiState.Error).message,
                        )
                    }
                }
            }
        }
        if (state.result is UiState.Loading) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
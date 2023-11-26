package com.mu.tote2024.presentation.screen.auth

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mu.tote2024.R
import com.mu.tote2024.presentation.components.AppTextField
import com.mu.tote2024.presentation.components.PasswordTextField
import com.mu.tote2024.presentation.ui.Tote2024Theme
import com.mu.tote2024.presentation.utils.Constants.MIN_PASSWORD_LENGTH

@Preview(
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
}

//@SuppressLint("UnrememberedMutableState", "SuspiciousIndentation")
@SuppressLint("UnrememberedMutableState")
@Composable
fun SignUpScreen(
    //viewModel: AuthViewModel = hiltViewModel()
    onSignUpClick: () -> Unit
) {
    var email by mutableStateOf<String?>(null)
    var password by mutableStateOf<String?>(null)
    var passwordConfirm by mutableStateOf<String?>(null)

    val isLoading by mutableStateOf(false)

    val isErrorEmail = mutableStateOf(true)
    val isErrorPassword = mutableStateOf(true)
    val isErrorPasswordConfirm = mutableStateOf(true)

    val enabledButton = mutableStateOf(false)

    /*val state by viewModel.state.collectAsState()
    Log.d(DEBUG_TAG, "start state: $state")*/

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
                    width = 1.dp,
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
                        value = email,
                        onChange = { newValue ->
                            isErrorEmail.value = newValue.isBlank()
                            enabledButton.value = (!isErrorEmail.value) && (!isErrorPassword.value) && (!isErrorPasswordConfirm.value)
                            email = newValue
                        },
                        label = stringResource(id = R.string.enter_email),
                        painterId = R.drawable.ic_mail,
                        description = "email",
                        errorMessage = stringResource(R.string.error_field_empty),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    PasswordTextField(
                        label = stringResource(id = R.string.enter_password),
                        value = password,
                        onChange = { newValue ->
                            isErrorPassword.value = newValue.isBlank() || newValue.length < MIN_PASSWORD_LENGTH
                            enabledButton.value = !isErrorEmail.value && !isErrorPassword.value && !isErrorPasswordConfirm.value
                            password = newValue
                        },
                        painterId = R.drawable.ic_password,
                        description = "password",
                        errorMessage = stringResource(R.string.error_min_length, MIN_PASSWORD_LENGTH)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    PasswordTextField(
                        label = stringResource(id = R.string.confirm_password),
                        value = passwordConfirm,
                        onChange = { newValue ->
                            isErrorPasswordConfirm.value = newValue.isBlank() || newValue.length < MIN_PASSWORD_LENGTH
                            enabledButton.value = !isErrorEmail.value && !isErrorPassword.value && !isErrorPasswordConfirm.value
                            passwordConfirm = newValue
                        },
                        painterId = R.drawable.ic_password,
                        description = "passwordConfirm",
                        errorMessage = stringResource(R.string.error_min_length, MIN_PASSWORD_LENGTH)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        enabled = !isErrorEmail.value && !isErrorPassword.value && !isErrorPasswordConfirm.value, //enabledButton.value,
                        onClick = {
                            /*viewModel.sendEvent(
                                AuthEvent.OnSignUp(
                                    email = email ?: "",
                                    password = password ?: ""
                                )
                            )*/
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.to_register)
                        )
                    }
                }
            }
        }
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
    //}
    //}
}
package com.mu.tote2024.presentation.screen.auth.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.usecase.auth_usecase.AuthUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.checkEmail
import com.mu.tote2024.presentation.utils.checkPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<SignUpState> = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    var signUpFields by mutableStateOf(
        SignInFields(
            email = "",
            password = "",
            passwordConfirm = "",
            errorEmail = null,
            errorPassword = null,
            errorPasswordConfirm = null,
        )
    )
        private set

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.OnEmailChange -> {
                signUpFields = signUpFields.copy(
                    email = event.email,
                    errorEmail = checkEmail(event.email)
                )
            }

            is SignUpEvent.OnPasswordChange -> {
                signUpFields = signUpFields.copy(
                    password = event.password,
                    errorPassword = checkPassword(event.password, signUpFields.passwordConfirm),
                    errorPasswordConfirm = checkPassword(signUpFields.passwordConfirm, event.password)
                )
            }

            is SignUpEvent.OnPasswordConfirmChange -> {
                signUpFields = signUpFields.copy(
                    passwordConfirm = event.passwordConfirm,
                    errorPasswordConfirm = checkPassword(event.passwordConfirm, signUpFields.password),
                    errorPassword = checkPassword(signUpFields.password, event.passwordConfirm)
                )
            }

            is SignUpEvent.OnSignUp -> {
                viewModelScope.launch {
                    authUseCase.signUp(event.email, event.password).collect {
                        _state.value = SignUpState(it)
                    }
                }
            }
        }
    }
}

data class SignUpState(
    val result: UiState<Boolean> = UiState.Default,
)

data class SignInFields(
    val email: String,
    val password: String,
    val passwordConfirm: String,
    val errorEmail: String?,
    val errorPassword: String?,
    val errorPasswordConfirm: String?,
)

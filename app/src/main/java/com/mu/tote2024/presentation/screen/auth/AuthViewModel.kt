package com.mu.tote2024.presentation.screen.auth

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
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<AuthState> = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    var authFields by mutableStateOf(
        AuthFields(
            email = "",
            password = "",
            passwordConfirm = "",
            errorEmail = null,
            errorPassword = null,
            errorPasswordConfirm = null,
        )
    )
        private set

    /*var email by mutableStateOf("")
        private set
    var errorEmail by mutableStateOf<String?>(null)
        private set
    var password by mutableStateOf("")
        private set
    var errorPassword by mutableStateOf<String?>(null)
        private set
    var passwordConfirm by mutableStateOf("")
        private set
    var errorPasswordConfirm by mutableStateOf<String?>(null)
        private set*/

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnEmailChange -> {
                authFields = authFields.copy(
                    email = event.email,
                    errorEmail = checkEmail(event.email)
                )
            }

            is AuthEvent.OnPasswordChange -> {
                authFields = authFields.copy(
                    password = event.password,
                    errorPassword = checkPassword(event.password, authFields.passwordConfirm),
                    errorPasswordConfirm = checkPassword(authFields.passwordConfirm, event.password)
                )
            }

            is AuthEvent.OnPasswordConfirmChange -> {
                authFields = authFields.copy(
                    passwordConfirm = event.passwordConfirm,
                    errorPasswordConfirm = checkPassword(event.passwordConfirm, authFields.password),
                    errorPassword = checkPassword(authFields.password, event.passwordConfirm)
                )
            }

            is AuthEvent.OnSignUp -> {
                viewModelScope.launch {
                    authUseCase.signUp(event.email, event.password).collect {
                        _state.value = AuthState(it)
                    }
                }
            }

            is AuthEvent.OnSignIn -> {}
            is AuthEvent.OnSignOut -> {}
        }
    }
}

data class AuthState(
    val result: UiState<Boolean> = UiState.Default,
)

data class AuthFields(
    val email: String,
    val password: String,
    val passwordConfirm: String,
    val errorEmail: String?,
    val errorPassword: String?,
    val errorPasswordConfirm: String?,
)

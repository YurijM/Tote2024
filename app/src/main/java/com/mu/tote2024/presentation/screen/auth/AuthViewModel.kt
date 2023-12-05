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

    var email by mutableStateOf("")
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
        private set

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnEmailChange -> {
                email = event.email
                errorEmail = checkEmail(email)
            }
            is AuthEvent.OnPasswordChange -> {
                password = event.password
                errorPassword = checkPassword(password, passwordConfirm)
                errorPasswordConfirm = checkPassword(passwordConfirm, password)
            }
            is AuthEvent.OnPasswordConfirmChange -> {
                passwordConfirm = event.passwordConfirm
                errorPasswordConfirm = checkPassword(passwordConfirm, password)
                errorPassword = checkPassword(password, passwordConfirm)
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

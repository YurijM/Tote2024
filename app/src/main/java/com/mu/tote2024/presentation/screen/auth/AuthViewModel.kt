package com.mu.tote2024.presentation.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.usecase.auth_usecase.AuthUseCase
import com.mu.tote2024.presentation.ui.common.UiState
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

    fun onEvent(event: AuthEvent) {
        when (event) {
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

package com.mu.tote2024.presentation.screen.auth.signin

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
class SignInViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<SignInState> = MutableStateFlow(SignInState())
    val state: StateFlow<SignInState> = _state.asStateFlow()

    var enabledButton by mutableStateOf(false)
        private set

    var signInFields by mutableStateOf(
        SignInFields(
            email = "",
            password = "",
            errorEmail = null,
            errorPassword = null,
        )
    )
        private set

    private val _mail = MutableStateFlow("")
    val mail = _mail.asStateFlow()

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.OnEmailChange -> {
                signInFields = signInFields.copy(
                    email = event.email,
                    errorEmail = checkEmail(event.email)
                )
                _mail.value = event.email
                enabledButton = checkValues()
            }

            is SignInEvent.OnPasswordChange -> {
                signInFields = signInFields.copy(
                    password = event.password,
                    errorPassword = checkPassword(event.password, null),
                )
                enabledButton = checkValues()
            }

            is SignInEvent.OnSignIn -> {
                viewModelScope.launch {
                    authUseCase.signIn(event.email, event.password).collect {
                        _state.value = SignInState(it)
                    }
                }
            }
        }
    }

    private fun checkValues(): Boolean {
        return (signInFields.errorEmail != null && signInFields.errorEmail!!.isBlank()) &&
                (signInFields.errorPassword != null && signInFields.errorPassword!!.isBlank())
    }

    companion object {
        data class SignInState(
            val result: UiState<Boolean> = UiState.Default,
        )

        data class SignInFields(
            val email: String,
            val password: String,
            val errorEmail: String?,
            val errorPassword: String?,
        )
    }
}

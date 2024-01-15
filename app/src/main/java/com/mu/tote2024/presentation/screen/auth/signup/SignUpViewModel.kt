package com.mu.tote2024.presentation.screen.auth.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.EmailModel
import com.mu.tote2024.domain.usecase.auth_usecase.AuthUseCase
import com.mu.tote2024.domain.usecase.gambler_usecase.GamblerUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.Errors.UNAUTHORIZED_EMAIL
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
    private val authUseCase: AuthUseCase,
    private val gamblerUseCase: GamblerUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<SignUpState> = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    var enabledButton by mutableStateOf(false)
        private set

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
                    errorEmail = checkEmail(event.email),
                )
                enabledButton = checkValues()
            }

            is SignUpEvent.OnPasswordChange -> {
                signUpFields = signUpFields.copy(
                    password = event.password,
                    errorPassword = checkPassword(event.password, signUpFields.passwordConfirm),
                    errorPasswordConfirm = checkPassword(signUpFields.passwordConfirm, event.password),
                )
                enabledButton = checkValues()
            }

            is SignUpEvent.OnPasswordConfirmChange -> {
                signUpFields = signUpFields.copy(
                    passwordConfirm = event.passwordConfirm,
                    errorPasswordConfirm = checkPassword(event.passwordConfirm, signUpFields.password),
                    errorPassword = checkPassword(signUpFields.password, event.passwordConfirm),
                )
                enabledButton = checkValues()
            }

            is SignUpEvent.OnSignUp -> {
                viewModelScope.launch {
                    gamblerUseCase.getEmailList().collect { state ->
                        if (state is UiState.Success<*>) {
                            val result = (state as UiState.Success<List<EmailModel>>).data
                            val emailIsCorrect = result.any { email -> email.email == event.email }

                            if (emailIsCorrect) {
                                authUseCase.signUp(event.email, event.password).collect {
                                    _state.value = SignUpState(it)
                                }
                            } else {
                                _state.value = SignUpState(UiState.Error(UNAUTHORIZED_EMAIL))
                            }
                        }
                    }
                }
            }
        }
    }
    private fun checkValues(): Boolean {
        return (signUpFields.errorEmail != null && signUpFields.errorEmail!!.isBlank()) &&
                (signUpFields.errorPassword != null && signUpFields.errorPassword!!.isBlank()) &&
                (signUpFields.errorPasswordConfirm != null && signUpFields.errorPasswordConfirm!!.isBlank())
    }

    companion object {
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
    }
}

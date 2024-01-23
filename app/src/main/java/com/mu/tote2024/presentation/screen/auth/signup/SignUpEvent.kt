package com.mu.tote2024.presentation.screen.auth.signup

sealed class SignUpEvent{
    data class OnEmailChange(val email: String) : SignUpEvent()
    data class OnPasswordChange(val password: String) : SignUpEvent()
    data class OnPasswordConfirmChange(val passwordConfirm: String) : SignUpEvent()
    object OnSignUp : SignUpEvent()
}

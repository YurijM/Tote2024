package com.mu.tote2024.presentation.screen.auth.signin

sealed class SignInEvent {
    data class OnEmailChange(val email: String) : SignInEvent()
    data class OnPasswordChange(val password: String) : SignInEvent()
    data class OnSignIn(val email: String, val password: String) : SignInEvent()
}

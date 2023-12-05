package com.mu.tote2024.presentation.screen.auth

sealed class AuthEvent{
    data class OnSignUp(val email: String, val password: String) : AuthEvent()
    data class OnSignIn(val email: String, val password: String) : AuthEvent()
    object OnSignOut : AuthEvent()
    data class OnEmailChange(val email: String) : AuthEvent()
    data class OnPasswordChange(val password: String) : AuthEvent()
    data class OnPasswordConfirmChange(val passwordConfirm: String) : AuthEvent()
}

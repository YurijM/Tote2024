package com.mu.tote2024.presentation.screen.auth

sealed class AuthEvent{
    data class OnSignUp(val email: String, val password: String) : AuthEvent()
    data class OnSignIn(val email: String, val password: String) : AuthEvent()
    object OnSignOut : AuthEvent()
}

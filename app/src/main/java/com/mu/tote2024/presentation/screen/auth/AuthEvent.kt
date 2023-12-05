package com.mu.tote2024.presentation.screen.auth

sealed class AuthEvent{
    object OnSignOut : AuthEvent()
}

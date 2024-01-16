package com.mu.tote2024.domain.usecase.auth_usecase

data class AuthUseCase (
    val signUp: SignUp,
    val signIn: SignIn,
    val getCurrentUserId: GetCurrentUserId
)
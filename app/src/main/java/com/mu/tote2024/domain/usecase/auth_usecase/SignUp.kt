package com.mu.tote2024.domain.usecase.auth_usecase

import com.mu.tote2024.domain.repository.AuthRepository

class SignUp(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.createGambler(email, password)
}
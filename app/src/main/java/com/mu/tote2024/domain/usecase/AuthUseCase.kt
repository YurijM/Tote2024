package com.mu.tote2024.domain.usecase

import com.mu.tote2024.domain.repository.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun createGambler(email: String, password: String) = authRepository.createGambler(email, password)
}
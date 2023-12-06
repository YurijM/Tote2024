package com.mu.tote2024.domain.usecase.auth_usecase

import com.mu.tote2024.domain.repository.AuthRepository

class GetGambler(
    private val authRepository: AuthRepository
) {
    operator fun invoke(id: String) =
        authRepository.getGambler(id)
}
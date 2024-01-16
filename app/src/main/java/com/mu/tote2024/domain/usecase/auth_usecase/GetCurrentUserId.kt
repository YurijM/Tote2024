package com.mu.tote2024.domain.usecase.auth_usecase

import com.mu.tote2024.domain.repository.AuthRepository

class GetCurrentUserId(
    private val authRepository: AuthRepository
) {
    operator fun invoke() =
        authRepository.getCurrentUserId()
}
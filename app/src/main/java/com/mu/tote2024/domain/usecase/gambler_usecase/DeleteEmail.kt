package com.mu.tote2024.domain.usecase.gambler_usecase

import com.mu.tote2024.domain.repository.GamblerRepository

class DeleteEmail(
    private val gamblerRepository: GamblerRepository
) {
    operator fun invoke(id: String) =
        gamblerRepository.deleteEmail(id)
}
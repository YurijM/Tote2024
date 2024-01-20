package com.mu.tote2024.domain.usecase.gambler_usecase

import com.mu.tote2024.domain.model.EmailModel
import com.mu.tote2024.domain.repository.GamblerRepository

class SaveEmail(
    private val gamblerRepository: GamblerRepository
) {
    operator fun invoke(email: EmailModel) =
        gamblerRepository.saveEmail(email)
}
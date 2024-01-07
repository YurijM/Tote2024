package com.mu.tote2024.domain.usecase.gambler_usecase

import com.mu.tote2024.domain.repository.GamblerRepository

class GetGamblerList(
    private val gamblerRepository: GamblerRepository
) {
    operator fun invoke() =
        gamblerRepository.getGamblerList()
}
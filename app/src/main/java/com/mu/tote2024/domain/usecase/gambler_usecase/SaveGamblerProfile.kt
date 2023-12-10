package com.mu.tote2024.domain.usecase.gambler_usecase

import com.mu.tote2024.domain.model.GamblerProfileModel
import com.mu.tote2024.domain.repository.GamblerRepository

class SaveGamblerProfile(
    private val gamblerRepository: GamblerRepository
) {
    operator fun invoke(profile: GamblerProfileModel) =
        gamblerRepository.saveGamblerProfile(profile)
}
package com.mu.tote2024.domain.usecase.stake_usecase

import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.domain.repository.StakeRepository

class SaveStake(
    private val stakeRepository: StakeRepository
) {
    operator fun invoke(stake: StakeModel) = stakeRepository.saveStake(stake)
}
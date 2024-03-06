package com.mu.tote2024.domain.usecase.stake_usecase

import com.mu.tote2024.domain.repository.StakeRepository

class GetStake(
    private val stakeRepository: StakeRepository
) {
    operator fun invoke(gameId: String, gamblerId: String) =
        stakeRepository.getStake(gameId, gamblerId)
}
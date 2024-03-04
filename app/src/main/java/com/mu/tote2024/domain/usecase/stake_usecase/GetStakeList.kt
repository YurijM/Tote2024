package com.mu.tote2024.domain.usecase.stake_usecase

import com.mu.tote2024.domain.repository.StakeRepository

class GetStakeList(
    private val stakeRepository: StakeRepository
) {
    operator fun invoke() = stakeRepository.getStakeList()
}
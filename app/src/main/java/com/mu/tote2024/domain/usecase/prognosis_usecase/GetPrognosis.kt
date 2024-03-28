package com.mu.tote2024.domain.usecase.prognosis_usecase

import com.mu.tote2024.domain.repository.PrognosisRepository

class GetPrognosis(
    private val prognosisRepository: PrognosisRepository
) {
    operator fun invoke(gameId: String) =
        prognosisRepository.getPrognosis(gameId)
}
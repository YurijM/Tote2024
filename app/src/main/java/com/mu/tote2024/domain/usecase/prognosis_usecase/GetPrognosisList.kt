package com.mu.tote2024.domain.usecase.prognosis_usecase

import com.mu.tote2024.domain.repository.PrognosisRepository

class GetPrognosisList(
    private val prognosisRepository: PrognosisRepository
) {
    operator fun invoke() = prognosisRepository.getPrognosisList()
}
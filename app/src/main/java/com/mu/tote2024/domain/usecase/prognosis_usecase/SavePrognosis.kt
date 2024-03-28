package com.mu.tote2024.domain.usecase.prognosis_usecase

import com.mu.tote2024.domain.model.PrognosisModel
import com.mu.tote2024.domain.repository.PrognosisRepository

class SavePrognosis(
    private val prognosisRepository: PrognosisRepository
) {
    operator fun invoke(prognosis: PrognosisModel) = prognosisRepository.savePrognosis(prognosis)
}
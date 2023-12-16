package com.mu.tote2024.domain.usecase.gambler_usecase

import android.net.Uri
import com.mu.tote2024.domain.repository.GamblerRepository

class SaveGamblerPhoto(
    private val gamblerRepository: GamblerRepository
) {
    operator fun invoke(id: String, photoUri: Uri) =
        gamblerRepository.saveGamblerPhoto(id, photoUri)
}
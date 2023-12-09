package com.mu.tote2024.domain.repository

import com.mu.tote2024.presentation.ui.common.UiState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun createGambler(
        email: String,
        password: String
    ): Flow<UiState<Boolean>>

    fun authGambler(
        email: String,
        password: String
    ): Flow<UiState<Boolean>>
}
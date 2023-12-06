package com.mu.tote2024.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.data.utils.Constants.CURRENT_ID
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.domain.usecase.auth_usecase.AuthUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    private val _gambler: MutableStateFlow<GamblerState> = MutableStateFlow(GamblerState())
    val gambler: StateFlow<GamblerState> = _gambler.asStateFlow()

    init {
        viewModelScope.launch {
            authUseCase.getGambler(CURRENT_ID).collect {
                _gambler.value = GamblerState(it)
            }
        }
    }

}

data class GamblerState(
    val result: UiState<GamblerModel> = UiState.Default,
)

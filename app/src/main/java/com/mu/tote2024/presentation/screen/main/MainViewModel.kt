package com.mu.tote2024.presentation.screen.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.data.utils.Constants.CURRENT_ID
import com.mu.tote2024.data.utils.Constants.GAMBLER
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.domain.usecase.gambler_usecase.GamblerUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val gamblerUseCase: GamblerUseCase
) : ViewModel() {
    private val _gambler: MutableStateFlow<GamblerState> = MutableStateFlow(GamblerState())
    val gambler: StateFlow<GamblerState> = _gambler.asStateFlow()

    var isGamblerAdmin by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            gamblerUseCase.getGambler(CURRENT_ID).collect {
                _gambler.value = GamblerState(it)
            }
        }
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnGamblerChange -> {
                isGamblerAdmin = GAMBLER.admin
            }
        }
    }

    companion object {
        data class GamblerState(
            val result: UiState<GamblerModel> = UiState.Default,
        )
    }
}

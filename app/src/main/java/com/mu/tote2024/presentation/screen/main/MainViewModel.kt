package com.mu.tote2024.presentation.screen.main

import android.util.Log
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
import com.mu.tote2024.presentation.utils.Constants.DEBUG_TAG
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
    private val _state: MutableStateFlow<GamblerState> = MutableStateFlow(GamblerState())
    val state: StateFlow<GamblerState> = _state.asStateFlow()

    /*private val _gambler: MutableStateFlow<GamblerModel> = MutableStateFlow(GAMBLER)
    val gambler: StateFlow<GamblerModel> = _gambler.asStateFlow()*/

    init {
        viewModelScope.launch {
            gamblerUseCase.getGambler(CURRENT_ID).collect {
                _state.value = GamblerState(it)
            }
         }
    }

    /*fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnGamblerChange -> {
                _state.value = GAMBLER
            }
        }
    }*/

    companion object {
        data class GamblerState(
            val result: UiState<GamblerModel> = UiState.Default,
        )
    }
}

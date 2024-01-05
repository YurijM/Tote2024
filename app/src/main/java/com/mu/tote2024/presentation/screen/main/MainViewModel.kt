package com.mu.tote2024.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.data.utils.Constants.CURRENT_ID
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_PROFILE_IS_EMPTY
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
    private val _state: MutableStateFlow<GamblerState> = MutableStateFlow(GamblerState())
    val state: StateFlow<GamblerState> = _state.asStateFlow()

    /*private val _gambler: MutableStateFlow<GamblerModel> = MutableStateFlow(GAMBLER)
    val gambler: StateFlow<GamblerModel> = _gambler.asStateFlow()*/

    init {
        viewModelScope.launch {
            gamblerUseCase.getGambler(CURRENT_ID).collect {
                _state.value = GamblerState(it)

                val currentValue = state.value.result
                if (currentValue is UiState.Success) {
                    if (GAMBLER.profile == null
                        || GAMBLER.profile!!.nickname.isBlank()
                        || GAMBLER.profile!!.photoUrl.isBlank()
                        || GAMBLER.profile!!.gender.isBlank()
                    ) {
                        _state.value = GamblerState(UiState.Error(ERROR_PROFILE_IS_EMPTY))
                    }
                }
            }
        }
    }

    /*fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnNavigateMain -> {
                sendUiEvent(UiEvent.NavigateMain(event.route))
            }

            else -> {}
        }
    }*/

    companion object {
        data class GamblerState(
            val result: UiState<GamblerModel> = UiState.Default,
        )
    }
}

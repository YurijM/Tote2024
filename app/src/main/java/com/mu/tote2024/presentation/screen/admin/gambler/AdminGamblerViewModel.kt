package com.mu.tote2024.presentation.screen.admin.gambler

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.domain.usecase.gambler_usecase.GamblerUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.Errors.FIELD_CAN_NOT_EMPTY
import com.mu.tote2024.presentation.utils.Constants.Errors.FIELD_CAN_NOT_NEGATIVE
import com.mu.tote2024.presentation.utils.Constants.KEY_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminGamblerViewModel @Inject constructor(
    private val gamblerUseCase: GamblerUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state: MutableStateFlow<AdminGamblerState> = MutableStateFlow(AdminGamblerState())
    val state: StateFlow<AdminGamblerState> = _state.asStateFlow()

    var errorRate by mutableStateOf("")
        private set

    var gambler by mutableStateOf(GamblerModel())
        private set

    init {
        val gamblerId = savedStateHandle.get<String>(KEY_ID)

        viewModelScope.launch {
            if (gamblerId != null) {
                gamblerUseCase.getGambler(gamblerId).collect { state ->
                    //_state.value = AdminGamblerState(state)
                    if (state is UiState.Success) {
                        gambler = state.data
                    }
                }
            }
        }
    }

    fun onEvent(event: AdminGamblerEvent) {
        when (event) {
            is AdminGamblerEvent.OnRateChange -> {
                errorRate = checkRate(event.rate)

                if (errorRate.isBlank()) {
                    gambler = gambler.copy(
                        rate = event.rate.toInt()
                    )
                }
            }

            is AdminGamblerEvent.OnIsAdminChange -> {
                gambler = gambler.copy(
                    admin = event.isAdmin
                )
            }

            is AdminGamblerEvent.OnCancel -> {
                _state.value = AdminGamblerState(UiState.Success(gambler))
            }

            is AdminGamblerEvent.OnSave -> {
                if (errorRate.isBlank()) {
                    viewModelScope.launch {
                        gamblerUseCase.saveGambler(gambler).collect {
                            _state.value = AdminGamblerState(it)
                        }
                    }
                } else {
                    _state.value = AdminGamblerState(UiState.Error(errorRate))
                }
            }
        }
    }

    private fun checkRate(rate: String): String {
        return when {
            rate.isBlank() -> FIELD_CAN_NOT_EMPTY
            rate.toInt() < 0 -> FIELD_CAN_NOT_NEGATIVE
            else -> ""
        }
    }

    companion object {
        data class AdminGamblerState(
            val result: UiState<GamblerModel> = UiState.Default,
        )
    }
}
package com.mu.tote2024.presentation.screen.admin.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.data.utils.Constants.CURRENT_ID
import com.mu.tote2024.data.utils.Constants.GAMBLER
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.domain.usecase.auth_usecase.AuthUseCase
import com.mu.tote2024.domain.usecase.gambler_usecase.GamblerUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.Errors.FIELD_CAN_NOT_EMPTY
import com.mu.tote2024.presentation.utils.Constants.Errors.FIELD_CAN_NOT_NEGATIVE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminProfileViewModel @Inject constructor(
    authUseCase: AuthUseCase,
    private val gamblerUseCase: GamblerUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<AdminProfileState> = MutableStateFlow(AdminProfileState())
    val state: StateFlow<AdminProfileState> = _state.asStateFlow()

    var errorRate by mutableStateOf("")
        private set

    var gambler by mutableStateOf(GamblerModel())
        private set

    init {
        CURRENT_ID = authUseCase.getCurrentUser()

        viewModelScope.launch {
            gamblerUseCase.getGambler(CURRENT_ID).collect {
                if (AdminProfileState(it).result is UiState.Success) {
                    gambler = GAMBLER
                }
            }
        }
    }

    fun onEvent(event: AdminProfileEvent) {
        when (event) {
            is AdminProfileEvent.OnRateChange -> {
                errorRate = checkRate(event.rate)

                if (errorRate.isBlank()) {
                    gambler = gambler.copy(
                        rate = event.rate.toInt()
                    )
                }
            }

            is AdminProfileEvent.OnIsAdminChange -> {
                gambler = gambler.copy(
                    admin = event.isAdmin
                )
            }

            is AdminProfileEvent.OnCancel -> {
                _state.value = AdminProfileState(UiState.Success(GAMBLER))
            }

            is AdminProfileEvent.OnSave -> {
                if (errorRate.isBlank()) {
                    viewModelScope.launch {
                        gamblerUseCase.saveGambler(gambler).collect {
                            _state.value = AdminProfileState(it)
                        }
                    }

                    GAMBLER = GAMBLER.copy(
                        rate = gambler.rate,
                        admin = gambler.admin
                    )
                } else {
                    _state.value = AdminProfileState(UiState.Error(errorRate))
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
        data class AdminProfileState(
            val result: UiState<GamblerModel> = UiState.Default,
        )
    }
}
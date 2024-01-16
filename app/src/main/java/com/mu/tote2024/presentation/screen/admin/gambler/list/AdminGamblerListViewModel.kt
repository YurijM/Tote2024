package com.mu.tote2024.presentation.screen.admin.gambler.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class AdminGamblerListViewModel @Inject constructor(
    private val gamblerUseCase: GamblerUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<AdminGamblerListState> = MutableStateFlow(AdminGamblerListState())
    val state: StateFlow<AdminGamblerListState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            gamblerUseCase.getGamblerList().collect {
                _state.value = AdminGamblerListState(it)
            }
        }
    }

    companion object {
        data class AdminGamblerListState(
            val result: UiState<List<GamblerModel>> = UiState.Default,
        )
    }
}
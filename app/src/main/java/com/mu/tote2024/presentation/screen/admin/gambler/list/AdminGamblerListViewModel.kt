package com.mu.tote2024.presentation.screen.admin.gambler.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.domain.model.GamblerResultModel
import com.mu.tote2024.domain.usecase.gambler_usecase.GamblerUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminGamblerListViewModel @Inject constructor(
    private val gamblerUseCase: GamblerUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<AdminGamblerListState> = MutableStateFlow(AdminGamblerListState())
    val state: StateFlow<AdminGamblerListState> = _state.asStateFlow()

    var gamblers = listOf<GamblerModel>()
        private set

    init {
        viewModelScope.launch {
            gamblerUseCase.getGamblerList().collect {
                if (it is UiState.Success) gamblers = it.data
                _state.value = AdminGamblerListState(it)
            }
        }
    }

    fun onEvent(event: AdminGamblerListEvent) {
        when (event) {
            is AdminGamblerListEvent.OnResultClear -> {
                gamblers.forEach { gambler ->
                    val newGambler = gambler.copy(
                        rate = (1..10).random() * 100,
                        result = GamblerResultModel()
                    )
                    gamblerUseCase.saveGambler(newGambler).onEach {}.launchIn(viewModelScope)
                }
            }
        }
    }

    companion object {
        data class AdminGamblerListState(
            val result: UiState<List<GamblerModel>> = UiState.Default,
        )
    }
}
package com.mu.tote2024.presentation.screen.admin.finish

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.FinishModel
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AdminFinishViewModel @Inject constructor(
    private val gameUseCase: GameUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<AdminFinishState> = MutableStateFlow(AdminFinishState())
    val state: StateFlow<AdminFinishState> = _state.asStateFlow()

    var finish by mutableStateOf(FinishModel())
        private set
    var isExit by mutableStateOf(false)
        private set

    init {
        gameUseCase.getFinish().onEach { finishState ->
            _state.value = AdminFinishState(finishState)
            if (finishState is UiState.Success) {
                finish = finishState.data
                isExit = false
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: AdminFinishEvent) {
        when (event) {
            is AdminFinishEvent.OnFinishChange -> {
                finish = finish.copy(
                    finish = event.finish
                )
            }

            is AdminFinishEvent.OnTextChange -> {
                finish = finish.copy(
                    text = event.text
                )
            }

            is AdminFinishEvent.OnCancel -> {
                _state.value = AdminFinishState(UiState.Success(finish))
                isExit = true
            }

            is AdminFinishEvent.OnSave -> {
                gameUseCase.saveFinish(finish).onEach { finishState ->
                    isExit = true
                    _state.value = AdminFinishState(finishState)
                }.launchIn(viewModelScope)
            }
        }
    }

    companion object {
        data class AdminFinishState(
            val result: UiState<FinishModel> = UiState.Default,
        )
    }
}
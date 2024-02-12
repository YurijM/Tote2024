package com.mu.tote2024.presentation.screen.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.Errors.FIELD_CAN_NOT_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameUseCase: GameUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<GameState> = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state

    var goal1 by mutableStateOf("")
        private set
    var goal2 by mutableStateOf("")
        private set
    var error by mutableStateOf("")
        private set

    fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.OnGoalChange -> {
                if (event.teamNo == 1) {
                    goal1 = event.goal
                } else {
                    goal2 = event.goal
                }
                error = ""
            }

            is GameEvent.OnSave -> {
                if (goal1.isNotBlank() && goal2.isNotBlank()) {
                    viewModelScope.launch {
                        gameUseCase.saveGame(event.game).collect { stateGame ->
                                _state.value = GameState(stateGame)
                        }
                    }
                } else {
                    error = FIELD_CAN_NOT_EMPTY
                }
            }
        }
    }

    companion object {
        data class GameState(
            val result: UiState<Boolean> = UiState.Default,
        )
    }
}
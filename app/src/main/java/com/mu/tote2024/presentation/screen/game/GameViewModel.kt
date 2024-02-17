package com.mu.tote2024.presentation.screen.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.KEY_ID
import com.mu.tote2024.presentation.utils.asTime
import com.mu.tote2024.presentation.utils.checkIsFieldEmpty
import com.mu.tote2024.presentation.utils.toLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameUseCase: GameUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _stateGame: MutableStateFlow<GameState> = MutableStateFlow(GameState())
    val stateGame: StateFlow<GameState> = _stateGame

    private val _state: MutableStateFlow<GameSaveState> = MutableStateFlow(GameSaveState())
    val state: StateFlow<GameSaveState> = _state

    var gameId by mutableStateOf(savedStateHandle.get<String>(KEY_ID))
        private set

    var game by mutableStateOf(GameModel())
        private set

    var errorGameId by mutableStateOf("")
        private set

    var disabled by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            gameUseCase.getGame(gameId ?: "").collect { state ->
                val result = GameState(state).result

                if (result is UiState.Success) {
                    game = result.data
                    disabled = checkValues()
                    toLog("start init: ${game.start.asTime()}")
                }

                _stateGame.value = GameState(state)
            }
        }
    }

    fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.OnGameIdChange -> {
                if (event.gameId.isNotBlank())
                    game = game.copy(gameId = event.gameId)
                //errorGameId = checkIsFieldEmpty(event.gameId)
            }

            is GameEvent.OnStartChange -> {
                game = game.copy(start = event.start)
                //errorStart = checkIsFieldEmpty(gameId)
                toLog("start viewModel: ${game.start.asTime()}")
            }

            is GameEvent.OnSave -> {
                /*if (goal1.isNotBlank() && goal2.isNotBlank()) {
                    viewModelScope.launch {
                        gameUseCase.saveGame(event.game).collect { state ->
                            _state.value = GameSaveState(state)
                        }
                    }
                } else {
                    error = FIELD_CAN_NOT_EMPTY
                }*/
            }

            else -> {}
        }
    }
    private fun checkValues(): Boolean {
        errorGameId = checkIsFieldEmpty(game.gameId)

        return (errorGameId.isBlank())
    }

    companion object {
        data class GameSaveState(
            val result: UiState<Boolean> = UiState.Default,
        )

        data class GameState(
            val result: UiState<GameModel> = UiState.Default,
        )
    }
}
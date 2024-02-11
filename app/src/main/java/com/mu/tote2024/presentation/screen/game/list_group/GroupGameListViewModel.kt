package com.mu.tote2024.presentation.screen.game.list_group

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants
import com.mu.tote2024.presentation.utils.Constants.Errors.FIELD_CAN_NOT_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupGameListViewModel @Inject constructor(
    private val gameUseCase: GameUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state: MutableStateFlow<GroupGameListState> = MutableStateFlow(GroupGameListState())
    val state: StateFlow<GroupGameListState> = _state

    private val _stateGame: MutableStateFlow<GameState> = MutableStateFlow(GameState())
    val stateGame: StateFlow<GameState> = _stateGame

    val group = savedStateHandle.get<String>(Constants.KEY_GROUP)

    var goal1 by mutableStateOf("")
        private set
    var goal2 by mutableStateOf("")
        private set
    var error by mutableStateOf("")
        private set

    init {

        viewModelScope.launch {
            gameUseCase.getGameList().collect { state ->
                _state.value = GroupGameListState(state)
            }
        }
    }

    fun onEvent(event: GroupGameListEvent) {
        when (event) {
            is GroupGameListEvent.OnGoalChange -> {
                if (event.teamNo == 1) {
                    goal1 = event.goal
                } else {
                    goal2 = event.goal
                }
                error = ""
            }

            is GroupGameListEvent.OnSave -> {
                if (goal1.isNotBlank() && goal2.isNotBlank()) {
                    viewModelScope.launch {
                        gameUseCase.saveGame(event.game).collect { stateGame ->
                            if (stateGame is UiState.Success) {
                                _stateGame.value = GameState(stateGame)
                            }
                        }
                    }
                } else {
                    error = FIELD_CAN_NOT_EMPTY
                }
            }
        }
    }

    companion object {
        data class GroupGameListState(
            val result: UiState<List<GameModel>> = UiState.Default,
        )
        data class GameState(
            val result: UiState<Boolean> = UiState.Default,
        )
    }
}
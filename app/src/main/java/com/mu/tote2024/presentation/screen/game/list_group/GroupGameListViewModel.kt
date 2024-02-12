package com.mu.tote2024.presentation.screen.game.list_group

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.GameFlagsModel
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.domain.usecase.team_usecase.TeamUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupGameListViewModel @Inject constructor(
    private val gameUseCase: GameUseCase,
    private val teamUseCase: TeamUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state: MutableStateFlow<GroupGameListState> = MutableStateFlow(GroupGameListState())
    val state: StateFlow<GroupGameListState> = _state

    val group = savedStateHandle.get<String>(Constants.KEY_GROUP)

    val listGameFlags = mutableListOf<GameFlagsModel>()

    init {
        viewModelScope.launch {
            teamUseCase.getTeamList().collect { stateTeams ->
                if (stateTeams is UiState.Success) {
                    val teams = stateTeams.data

                    gameUseCase.getGameList().collect { state ->
                        if (state is UiState.Success) {
                            val games = state.data.filter { it.group == group }
                            games.forEach { game ->
                                val flag1 = teams.first { it.team == game.team1 }.flag
                                val flag2 = teams.first { it.team == game.team2 }.flag
                                listGameFlags.add(
                                    GameFlagsModel(
                                        gameId = game.gameId,
                                        flag1 = flag1,
                                        flag2 = flag2
                                    )
                                )
                            }
                        }
                        _state.value = GroupGameListState(state)
                    }
                }
            }
        }
    }

    companion object {
        data class GroupGameListState(
            val result: UiState<List<GameModel>> = UiState.Default,
        )
    }
}
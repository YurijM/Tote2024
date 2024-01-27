package com.mu.tote2024.presentation.screen.admin.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.GameFlagsModel
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.domain.usecase.team_usecase.TeamUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.convertDateTimeToTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminGameListViewModel @Inject constructor(
    private val gameUseCase: GameUseCase,
    private val teamUseCase: TeamUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<AdminGameListState> = MutableStateFlow(AdminGameListState())
    val state: StateFlow<AdminGameListState> = _state

    val listGameFlags = mutableListOf<GameFlagsModel>()

    init {
        viewModelScope.launch {
            teamUseCase.getTeamList().collect { stateTeams ->
                if (stateTeams is UiState.Success) {
                    val teams = stateTeams.data

                    gameUseCase.getGameList().collect { state ->
                        if (state is UiState.Success) {
                            val games = state.data
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
                            _state.value = AdminGameListState(state)
                        } else {
                            _state.value = AdminGameListState(state)
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: AdminGameListEvent) {
        when (event) {
            is AdminGameListEvent.OnLoad -> {
                games.forEach { game ->
                    if (game.gameId.isNotBlank()) {
                        viewModelScope.launch {
                            gameUseCase.saveGame(game).collect {}
                        }
                    }
                }
            }
        }
    }

    companion object {
        data class AdminGameListState(
            val result: UiState<List<GameModel>> = UiState.Default
        )

        val games = listOf(
            GameModel(
                gameId = "1",
                start = convertDateTimeToTimestamp("14.06.2024 22:00"),
                group = "A",
                team1 = "Германия",
                team2 = "Шотландия"
            ),
            GameModel(
                gameId = "2",
                start = convertDateTimeToTimestamp("15.06.2024 16:00"),
                group = "A",
                team1 = "Венгрия",
                team2 = "Швейцария"
            ),
            GameModel(
                gameId = "3",
                start = convertDateTimeToTimestamp("15.06.2024 19:00"),
                group = "B",
                team1 = "Испания",
                team2 = "Хорватия"
            ),
            GameModel(
                gameId = "4",
                start = convertDateTimeToTimestamp("15.06.2024 22:00"),
                group = "B",
                team1 = "Италия",
                team2 = "Албания"
            ),
            GameModel(
                gameId = "6",
                start = convertDateTimeToTimestamp("16.06.2024 19:00"),
                group = "C",
                team1 = "Словения",
                team2 = "Дания"
            ),
        )
    }
}
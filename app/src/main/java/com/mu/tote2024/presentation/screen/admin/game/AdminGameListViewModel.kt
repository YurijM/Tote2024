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
            /*GameModel(
                gameId = "5",
                start = convertDateTimeToTimestamp("16.06.2024 16:00"),
                group = "D",
                team1 = "",
                team2 = "Нидерланды"
            ),*/
            GameModel(
                gameId = "6",
                start = convertDateTimeToTimestamp("16.06.2024 19:00"),
                group = "C",
                team1 = "Словения",
                team2 = "Дания"
            ),
            GameModel(
                gameId = "7",
                start = convertDateTimeToTimestamp("16.06.2024 22:00"),
                group = "C",
                team1 = "Сербия",
                team2 = "Англия"
            ),
            /*GameModel(
                gameId = "8",
                start = convertDateTimeToTimestamp("17.06.2024 16:00"),
                group = "E",
                team1 = "Румыния",
                team2 = ""
            ),*/
            GameModel(
                gameId = "9",
                start = convertDateTimeToTimestamp("17.06.2024 19:00"),
                group = "E",
                team1 = "Бельгия",
                team2 = "Словакия"
            ),
            GameModel(
                gameId = "10",
                start = convertDateTimeToTimestamp("17.06.2024 22:00"),
                group = "D",
                team1 = "Австрия",
                team2 = "Франция"
            ),
            /*GameModel(
                gameId = "11",
                start = convertDateTimeToTimestamp("18.06.2024 19:00"),
                group = "F",
                team1 = "Турция",
                team2 = ""
            ),*/
            GameModel(
                gameId = "12",
                start = convertDateTimeToTimestamp("18.06.2024 22:00"),
                group = "F",
                team1 = "Португалия",
                team2 = "Чехия"
            ),
            GameModel(
                gameId = "13",
                start = convertDateTimeToTimestamp("19.06.2024 16:00"),
                group = "B",
                team1 = "Хорватия",
                team2 = "Албания"
            ),
            GameModel(
                gameId = "14",
                start = convertDateTimeToTimestamp("19.06.2024 19:00"),
                group = "A",
                team1 = "Германия",
                team2 = "Венгрия"
            ),
            GameModel(
                gameId = "15",
                start = convertDateTimeToTimestamp("19.06.2024 22:00"),
                group = "A",
                team1 = "Шотландия",
                team2 = "Швейцария"
            ),
            GameModel(
                gameId = "16",
                start = convertDateTimeToTimestamp("20.06.2024 16:00"),
                group = "C",
                team1 = "Словения",
                team2 = "Сербия"
            ),
            GameModel(
                gameId = "17",
                start = convertDateTimeToTimestamp("20.06.2024 19:00"),
                group = "C",
                team1 = "Дания",
                team2 = "Англия"
            ),
            GameModel(
                gameId = "18",
                start = convertDateTimeToTimestamp("20.06.2024 22:00"),
                group = "B",
                team1 = "Испания",
                team2 = "Италия"
            ),
            /*GameModel(
                gameId = "19",
                start = convertDateTimeToTimestamp("21.06.2024 16:00"),
                group = "E",
                team1 = "Словакия",
                team2 = ""
            ),*/
            /*GameModel(
                gameId = "20",
                start = convertDateTimeToTimestamp("21.06.2024 19:00"),
                group = "D",
                team1 = "",
                team2 = "Австрия"
            ),*/
            GameModel(
                gameId = "21",
                start = convertDateTimeToTimestamp("21.06.2024 22:00"),
                group = "D",
                team1 = "Нидерланды",
                team2 = "Франция"
            ),
            /*GameModel(
                gameId = "22",
                start = convertDateTimeToTimestamp("22.06.2024 16:00"),
                group = "F",
                team1 = "",
                team2 = "Чехия"
            ),*/
            GameModel(
                gameId = "23",
                start = convertDateTimeToTimestamp("22.06.2024 19:00"),
                group = "F",
                team1 = "Турция",
                team2 = "Португалия"
            ),
            GameModel(
                gameId = "24",
                start = convertDateTimeToTimestamp("22.06.2024 22:00"),
                group = "E",
                team1 = "Бельгия",
                team2 = "Румыния"
            ),
            GameModel(
                gameId = "25",
                start = convertDateTimeToTimestamp("23.06.2024 22:00"),
                group = "A",
                team1 = "Швейцария",
                team2 = "Германия"
            ),
            GameModel(
                gameId = "26",
                start = convertDateTimeToTimestamp("23.06.2024 22:00"),
                group = "A",
                team1 = "Шотландия",
                team2 = "Венгрия"
            ),
            GameModel(
                gameId = "27",
                start = convertDateTimeToTimestamp("24.06.2024 22:00"),
                group = "B",
                team1 = "Албания",
                team2 = "Испания"
            ),
            GameModel(
                gameId = "28",
                start = convertDateTimeToTimestamp("24.06.2024 22:00"),
                group = "B",
                team1 = "Хорватия",
                team2 = "Италия"
            ),
            GameModel(
                gameId = "29",
                start = convertDateTimeToTimestamp("25.06.2024 19:00"),
                group = "D",
                team1 = "Нидерланды",
                team2 = "Австрия"
            ),
            /*GameModel(
                gameId = "30",
                start = convertDateTimeToTimestamp("25.06.2024 19:00"),
                group = "D",
                team1 = "Франция",
                team2 = ""
            ),*/
            GameModel(
                gameId = "31",
                start = convertDateTimeToTimestamp("25.06.2024 22:00"),
                group = "C",
                team1 = "Англия",
                team2 = "Словения"
            ),
            GameModel(
                gameId = "32",
                start = convertDateTimeToTimestamp("25.06.2024 22:00"),
                group = "C",
                team1 = "Дания",
                team2 = "Сербия"
            ),
            GameModel(
                gameId = "33",
                start = convertDateTimeToTimestamp("26.06.2024 19:00"),
                group = "E",
                team1 = "Словакия",
                team2 = "Румыния"
            ),
            /*GameModel(
                gameId = "34",
                start = convertDateTimeToTimestamp("26.06.2024 19:00"),
                group = "E",
                team1 = "",
                team2 = "Бельгия"
            ),*/
            /*GameModel(
                gameId = "35",
                start = convertDateTimeToTimestamp("26.06.2024 22:00"),
                group = "F",
                team1 = "",
                team2 = "Португалия"
            ),*/
            GameModel(
                gameId = "36",
                start = convertDateTimeToTimestamp("26.06.2024 22:00"),
                group = "F",
                team1 = "Чехия",
                team2 = "Турция"
            ),
        )
    }
}
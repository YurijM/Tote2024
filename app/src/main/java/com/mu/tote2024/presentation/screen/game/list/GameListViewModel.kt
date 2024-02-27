package com.mu.tote2024.presentation.screen.game.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.GameFlagsModel
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.model.GroupTeamResultModel
import com.mu.tote2024.domain.model.TeamModel
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.domain.usecase.team_usecase.TeamUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.EMPTY
import com.mu.tote2024.presentation.utils.Constants.GROUPS
import com.mu.tote2024.presentation.utils.Constants.GROUPS_COUNT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    private val gameUseCase: GameUseCase,
    private val teamUseCase: TeamUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<GameListState> = MutableStateFlow(GameListState())
    val state: StateFlow<GameListState> = _state

    var games = listOf<GameModel>()
    val flags = mutableListOf<GameFlagsModel>()

    init {
        viewModelScope.launch {
            teamUseCase.getTeamList().collect { stateTeams ->
                _state.value = GameListState(UiState.Loading)

                if (stateTeams is UiState.Success) {
                    val teams = stateTeams.data
                        .sortedWith(
                            compareBy<TeamModel> { it.group }
                                .thenBy { it.itemNo }
                        )

                    gameUseCase.getGameList().collect { stateGame ->
                        if (stateGame is UiState.Success) {
                            games = stateGame.data

                            games.forEach { game ->
                                if (
                                    game.start.toLong() <= System.currentTimeMillis()
                                    && game.goal1.isBlank()
                                    && game.goal2.isBlank()
                                ) {
                                    val correctedGame = game.copy(
                                        goal1 = "0",
                                        goal2 = "0"
                                    )
                                    gameUseCase.saveGame(correctedGame).launchIn(viewModelScope)
                                }
                                val flag1 = teams.first { it.team == game.team1 }.flag
                                val flag2 = teams.first { it.team == game.team2 }.flag
                                flags.add(
                                    GameFlagsModel(
                                        gameId = game.gameId,
                                        flag1 = flag1,
                                        flag2 = flag2
                                    )
                                )
                            }
                            val resultTeams = getTableResult(
                                teams,
                                games.filter { game -> game.group in GROUPS.take(GROUPS_COUNT) }
                            )

                            _state.value = GameListState(UiState.Success(resultTeams))
                        }
                    }
                }
            }
        }
    }

    private fun getTableResult(listTeam: List<TeamModel>, games: List<GameModel>): List<GroupTeamResultModel> {
        val resultTeams = mutableListOf<GroupTeamResultModel>()
        listTeam.forEach { team ->
            var resultTeam = GroupTeamResultModel().copy(
                group = team.group,
                team = team.team,
                teamNo = team.itemNo
            )

            var win = 0
            var draw = 0
            var defeat = 0
            var balls1 = 0
            var balls2 = 0
            var points = 0

            games.filter { it.team1 == team.team || it.team2 == team.team }
                .forEach { game ->
                    val goal1: String
                    val goal2: String
                    var score = ""
                    val team2ItemNo: Int
                    val teamNo = if (game.team1 == team.team) 1 else 2

                    if (teamNo == 1) {
                        goal1 = game.goal1
                        goal2 = game.goal2
                        team2ItemNo = listTeam.first { it.team == game.team2 }.itemNo
                    } else {
                        goal1 = game.goal2
                        goal2 = game.goal1
                        team2ItemNo = listTeam.first { it.team == game.team1 }.itemNo
                    }

                    if (goal1.isNotBlank() && goal2.isNotBlank()) {
                        score = "$goal1:$goal2"
                        balls1 += goal1.toInt()
                        balls2 += goal2.toInt()

                        when {
                            goal1 > goal2 -> {
                                win++
                                points += 3
                            }

                            goal1 == goal2 -> {
                                draw++
                                points++
                            }

                            else -> defeat++
                        }
                    }

                    resultTeam = when (team2ItemNo) {
                        1 -> resultTeam.copy(score1 = score)
                        2 -> resultTeam.copy(score2 = score)
                        3 -> resultTeam.copy(score3 = score)
                        4 -> resultTeam.copy(score4 = score)
                        else -> resultTeam
                    }

                    resultTeam = when (team.itemNo) {
                        1 -> resultTeam.copy(score1 = EMPTY)
                        2 -> resultTeam.copy(score2 = EMPTY)
                        3 -> resultTeam.copy(score3 = EMPTY)
                        4 -> resultTeam.copy(score4 = EMPTY)
                        else -> resultTeam
                    }
                }

            resultTeam = resultTeam.copy(
                win = win,
                draw = draw,
                defeat = defeat,
                balls1 = balls1,
                balls2 = balls2,
                points = points,
            )

            resultTeams.add(resultTeam)
        }

        return resultTeams
    }

    companion object {
        data class GameListState(
            val result: UiState<List<GroupTeamResultModel>> = UiState.Default
        )
    }
}


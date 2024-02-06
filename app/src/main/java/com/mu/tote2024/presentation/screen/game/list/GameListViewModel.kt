package com.mu.tote2024.presentation.screen.game.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.model.GroupResultModel
import com.mu.tote2024.domain.model.TeamModel
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.domain.usecase.team_usecase.TeamUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants
import com.mu.tote2024.presentation.utils.toLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    private val gameUseCase: GameUseCase,
    private val teamUseCase: TeamUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<GameListState> = MutableStateFlow(GameListState())
    val state: StateFlow<GameListState> = _state

    var listTeam = listOf<TeamModel>()

    init {
        viewModelScope.launch {
            /*teamUseCase.getTeamList().collect { stateTeams ->
                if (stateTeams is UiState.Success) {
                    listTeam = stateTeams.data

                    gameUseCase.getGameList().collect { state ->
                        _state.value = GameListState(state)
                    }
                }
            }*/

            teamUseCase.getTeamList().collect { stateTeams ->
                if (stateTeams is UiState.Success) {
                    listTeam = stateTeams.data
                        .sortedWith(
                            compareBy<TeamModel> { it.group }
                                .thenBy { it.itemNo }
                        )

                    gameUseCase.getGameList().collect { state ->
                        if (state is UiState.Success) {
                            val listGame = state.data

                            (1..Constants.GROUPS_COUNT).forEach { index ->
                                val group = listTeam[(index - 1) * Constants.TEAMS_COUNT_INTO_GROUP].group
                                toLog("group: $group")
                                val listGroupTeams = listTeam.filter { team -> team.group == group }
                                val listGroupGames = listGame.filter { team -> team.group == group }

                                listGroupTeams.forEach { team ->
                                    val result = mutableListOf<GroupResultModel>()
                                    (team.itemNo + 1..Constants.TEAMS_COUNT_INTO_GROUP).forEach { itemNo ->
                                        val teamNext = listGroupTeams.first { it.itemNo == itemNo }
                                        val game = listGroupGames.first { game ->
                                            (game.team1 == team.team || game.team2 == team.team)
                                                    && (game.team1 == teamNext.team || game.team2 == teamNext.team)
                                        }
                                        if (game.team1 == team.team) {
                                            result.add(
                                                GroupResultModel(
                                                    group = group,
                                                    teamNo = team.itemNo,
                                                    gameNo = itemNo,
                                                    goal1 = game.goal1,
                                                    goal2 = game.goal2,
                                                )
                                            )
                                            result.add(
                                                GroupResultModel(
                                                    group = group,
                                                    teamNo = itemNo,
                                                    gameNo = team.itemNo,
                                                    goal1 = game.goal2,
                                                    goal2 = game.goal1,
                                                )
                                            )
                                        } else {
                                            result.add(
                                                GroupResultModel(
                                                    group = group,
                                                    teamNo = team.itemNo,
                                                    gameNo = itemNo,
                                                    goal1 = game.goal2,
                                                    goal2 = game.goal1,
                                                )
                                            )
                                            result.add(
                                                GroupResultModel(
                                                    group = group,
                                                    teamNo = itemNo,
                                                    gameNo = team.itemNo,
                                                    goal1 = game.goal1,
                                                    goal2 = game.goal2,
                                                )
                                            )
                                        }
                                    }

                                    toLog("result: ${
                                        result.sortedWith(
                                            compareBy<GroupResultModel> { it.teamNo }
                                                .thenBy { it.gameNo }
                                        )
                                    }")
                                }
                            }
                        }
                        _state.value = GameListState(state)
                    }
                }
            }
        }
    }

    companion object {
        data class GameListState(
            val result: UiState<List<GameModel>> = UiState.Default
        )
    }
}
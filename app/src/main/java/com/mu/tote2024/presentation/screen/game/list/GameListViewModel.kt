package com.mu.tote2024.presentation.screen.game.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.model.GroupResultModel
import com.mu.tote2024.domain.model.GroupTeamResultModel
import com.mu.tote2024.domain.model.TeamModel
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.domain.usecase.team_usecase.TeamUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants
import com.mu.tote2024.presentation.utils.Constants.EMPTY
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

    var resultTeams = mutableListOf<GroupTeamResultModel>()

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
                    val listTeam = stateTeams.data
                        .sortedWith(
                            compareBy<TeamModel> { it.group }
                                .thenBy { it.itemNo }
                        )

                    gameUseCase.getGameList().collect { state ->
                        if (state is UiState.Success) {
                            val listGame = state.data

                            var result = mutableListOf<GroupResultModel>()

                            (1..Constants.GROUPS_COUNT).forEach { index ->
                                val group = listTeam[(index - 1) * Constants.TEAMS_COUNT_INTO_GROUP].group
                                val listGroupTeams = listTeam.filter { team -> team.group == group }
                                val listGroupGames = listGame.filter { game -> game.group == group }

                                listGroupTeams.forEach { team ->
                                    result.add(
                                        //результат "сам с собой"
                                        GroupResultModel(
                                            group = group,
                                            team = team.team,
                                            teamNo = team.itemNo,
                                            gameNo = team.itemNo,
                                            goal1 = EMPTY,
                                            goal2 = EMPTY,
                                        )
                                    )

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
                                                    team = team.team,
                                                    teamNo = team.itemNo,
                                                    gameNo = itemNo,
                                                    goal1 = game.goal1,
                                                    goal2 = game.goal2,
                                                )
                                            )
                                            result.add(
                                                GroupResultModel(
                                                    group = group,
                                                    team = teamNext.team,
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
                                                    team = team.team,
                                                    teamNo = team.itemNo,
                                                    gameNo = itemNo,
                                                    goal1 = game.goal2,
                                                    goal2 = game.goal1,
                                                )
                                            )
                                            result.add(
                                                GroupResultModel(
                                                    group = group,
                                                    team = teamNext.team,
                                                    teamNo = itemNo,
                                                    gameNo = team.itemNo,
                                                    goal1 = game.goal1,
                                                    goal2 = game.goal2,
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            result = result.sortedWith(
                                compareBy<GroupResultModel> { it.teamNo }
                                    .thenBy { it.gameNo }
                            ).toMutableList()
                            val groups = arrayListOf("A", "B", "C", "D", "E", "F")
                            var teamResult = GroupTeamResultModel()

                            (1..Constants.GROUPS_COUNT).forEach { index ->
                                val group = groups[index - 1]
                                var team = ""
                                var score: String

                                if (teamResult.group.isNotBlank()) {
                                    toLog("teamResult: $teamResult")
                                    resultTeams.add(teamResult)
                                }

                                result.filter { it.group == group }.forEach {
                                    if (it.team != team) {
                                        if (team.isNotBlank()) {
                                            toLog("teamResult: $teamResult")
                                            resultTeams.add(teamResult)
                                        }

                                        team = it.team

                                        teamResult = GroupTeamResultModel().copy(
                                            group = group,
                                            team = team,
                                            teamNo = it.teamNo,
                                        )
                                    }

                                    if (it.goal1.isNotBlank() && it.goal1 != EMPTY
                                        && it.goal2.isNotBlank() && it.goal2 != EMPTY
                                    ) {
                                        score = "${it.goal1}:${it.goal2}"

                                        teamResult = when {
                                            it.goal1 > it.goal2 -> {
                                                teamResult.copy(
                                                    win = teamResult.win + 1,
                                                    balls1 = teamResult.balls1 + it.goal1.toInt(),
                                                    balls2 = teamResult.balls2 + it.goal2.toInt(),
                                                    points = teamResult.points + 3
                                                )
                                            }
                                            it.goal1 == it.goal2 ->
                                                teamResult.copy(
                                                    draw = teamResult.draw + 1,
                                                    balls1 = teamResult.balls1 + it.goal1.toInt(),
                                                    balls2 = teamResult.balls2 + it.goal2.toInt(),
                                                    points = teamResult.points + 1
                                                )
                                            else -> teamResult.copy(
                                                defeat = teamResult.defeat + 1,
                                                balls1 = teamResult.balls1 + it.goal1.toInt(),
                                                balls2 = teamResult.balls2 + it.goal2.toInt()
                                            )
                                        }
                                    } else if (it.teamNo == it.gameNo) {
                                        score = EMPTY
                                    } else {
                                        score = ""
                                    }
                                    teamResult = when (it.gameNo) {
                                        1 -> teamResult.copy(score1 = score)
                                        2 -> teamResult.copy(score2 = score)
                                        3 -> teamResult.copy(score3 = score)
                                        4 -> teamResult.copy(score4 = score)
                                        else -> teamResult
                                    }
                                }
                            }
                            toLog("teamResult: $teamResult")
                            resultTeams.add(teamResult)

                            resultTeams.sortedWith(
                                compareBy<GroupTeamResultModel> { it.group }
                                    .thenBy { it.teamNo }
                            ).forEach { item ->
                                toLog(item.toString())
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
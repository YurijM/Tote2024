package com.mu.tote2024.presentation.screen.stake.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.data.utils.Constants.CURRENT_ID
import com.mu.tote2024.domain.model.GameFlagsModel
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.domain.usecase.stake_usecase.StakeUseCase
import com.mu.tote2024.domain.usecase.team_usecase.TeamUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StakeListViewModel @Inject constructor(
    private val stakeUseCase: StakeUseCase,
    private val gameUseCase: GameUseCase,
    private val teamUseCase: TeamUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<StakeListState> = MutableStateFlow(StakeListState())
    val state: StateFlow<StakeListState> = _state

    private var stakes = mutableListOf<StakeModel>()
    val flags = mutableListOf<GameFlagsModel>()

    val listStart = mutableListOf<StartList>()

    init {
        viewModelScope.launch {
            teamUseCase.getTeamList().collect { stateTeams ->
                _state.value = StakeListState(UiState.Loading)

                if (stateTeams is UiState.Success) {
                    val teams = stateTeams.data

                    gameUseCase.getGameList().collect { stateGame ->
                        if (stateGame is UiState.Success) {
                            val games = stateGame.data.filter { it.start.toDouble() > System.currentTimeMillis() }
                            val gameIds = arrayListOf<String>()
                            games.forEach { game ->
                                gameIds.add(game.gameId)

                                val flag1 = teams.first { it.team == game.team1 }.flag
                                val flag2 = teams.first { it.team == game.team2 }.flag
                                flags.add(
                                    GameFlagsModel(
                                        gameId = game.gameId,
                                        flag1 = flag1,
                                        flag2 = flag2
                                    )
                                )
                                listStart.add(
                                    StartList(
                                        gameId = game.gameId,
                                        start = game.start
                                    )
                                )
                            }

                            stakeUseCase.getStakeList().collect { stateStake ->
                                if (stateStake is UiState.Success) {
                                    stakes = stateStake.data.filter {
                                        it.gamblerId == CURRENT_ID
                                                && it.gameId in gameIds
                                    }.toMutableList()

                                    games.forEach { game ->
                                        if (stakes.find { it.gameId == game.gameId } == null) {
                                            val stake = StakeModel(
                                                gameId = game.gameId,
                                                gamblerId = CURRENT_ID,
                                                //gamblerNick = GAMBLER.profile.nickname,
                                                group = game.group,
                                                team1 = game.team1,
                                                team2 = game.team2
                                            )
                                            stakes.add(stake)
                                        }
                                    }
                                    stakes = stakes.sortedBy { it.gameId.toInt() }.toMutableList()
                                    _state.value = StakeListState(UiState.Success(stakes))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        data class StakeListState(
            val result: UiState<List<StakeModel>> = UiState.Default
        )

        data class StartList(
            val gameId: String,
            val start: String
        )
    }
}
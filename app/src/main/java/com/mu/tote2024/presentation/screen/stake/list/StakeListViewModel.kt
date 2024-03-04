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
import com.mu.tote2024.presentation.utils.toLog
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

    var stakes = mutableListOf<StakeModel>()
    val flags = mutableListOf<GameFlagsModel>()

    init {
        viewModelScope.launch {
            _state.value = StakeListState(UiState.Loading)
            teamUseCase.getTeamList().collect { stateTeams ->
                //_state.value = StakeListState(UiState.Loading)

                if (stateTeams is UiState.Success) {
                    val teams = stateTeams.data

                    stakeUseCase.getStakeList().collect { stateStake ->
                        if (stateStake is UiState.Success) {
                            stakes = stateStake.data.filter { it.gamblerId == CURRENT_ID }.toMutableList()

                            gameUseCase.getGameList().collect { stateGame ->
                                if (stateGame is UiState.Success) {
                                    toLog("stateGame is Success")
                                    stateGame.data
                                        .filter { it.start.toLong() > System.currentTimeMillis() }
                                        .forEach { game ->
                                            if (stakes.find { it.gameId == game.gameId } == null) {
                                                val stake = StakeModel(
                                                    gameId = game.gameId,
                                                    gamblerId = CURRENT_ID,
                                                    start = game.start,
                                                    group = game.group,
                                                    team1 = game.team1,
                                                    team2 = game.team2
                                                )
                                                stakes.add(stake)
                                            }
                                        }

                                    _state.value = StakeListState(UiState.Success(true))
                                    toLog("state after stakes loading: ${state.value.result}")
                                }
                            }
                        }
                    }
                }
                toLog("state ViewModel: ${state.value.result}")
            }
        }
    }

    companion object {
        data class StakeListState(
            val result: UiState<Boolean> = UiState.Default
        )
    }
}
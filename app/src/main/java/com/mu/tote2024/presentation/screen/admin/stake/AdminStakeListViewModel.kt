package com.mu.tote2024.presentation.screen.admin.stake

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.domain.model.GameFlagsModel
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.domain.usecase.gambler_usecase.GamblerUseCase
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.domain.usecase.stake_usecase.StakeUseCase
import com.mu.tote2024.domain.usecase.team_usecase.TeamUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminStakeListViewModel @Inject constructor(
    gameUseCase: GameUseCase,
    gamblerUseCase: GamblerUseCase,
    private val stakeUseCase: StakeUseCase,
    private val teamUseCase: TeamUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<AdminStakeListState> = MutableStateFlow(AdminStakeListState())
    val state: StateFlow<AdminStakeListState> = _state

    val listGameFlags = mutableListOf<GameFlagsModel>()
    private var games = mutableListOf<GameModel>()
    val gameIds = mutableListOf<Int>()
    var gamblers = mutableListOf<GamblerModel>()

    init {
        viewModelScope.launch {
            teamUseCase.getTeamList().collect { stateTeams ->
                if (stateTeams is UiState.Success) {
                    val teams = stateTeams.data

                    stakeUseCase.getStakeList().collect { state ->
                        if (state is UiState.Success) {
                            val stakes = state.data
                            stakes.forEach { game ->
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
                        _state.value = AdminStakeListState(state)
                    }
                }
            }
        }
        gameUseCase.getGameList().onEach { stateGame ->
            if (stateGame is UiState.Success) {
                games = stateGame.data.filter { it.start.toDouble() > System.currentTimeMillis() }.sortedBy { it.gameId }.toMutableList()
                gameIds.clear()
                games.forEach { game ->
                    gameIds.add(game.gameId.toInt())
                }
            }
        }.launchIn(viewModelScope)
        gamblerUseCase.getGamblerList().onEach { stateGambler ->
            if (stateGambler is UiState.Success) {
                gamblers = stateGambler.data.sortedBy { it.profile.nickname }.toMutableList()
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: AdminStakeListEvent) {
        when (event) {
            is AdminStakeListEvent.OnGenerate -> {
                _state.value = AdminStakeListState(UiState.Loading)

                games.forEach { game ->
                    gamblers.filter { it.rate > 0 }.forEach { gambler ->
                        val stake = StakeModel(
                            gameId = game.gameId,
                            gamblerId = gambler.gamblerId ?: "",
                            group = game.group,
                            team1 = game.team1,
                            team2 = game.team2,
                            goal1 = (0..3).random().toString(),
                            goal2 = (0..3).random().toString(),
                        )
                        viewModelScope.launch {
                            stakeUseCase.saveStake(stake).collect {}
                        }
                    }
                }
            }
        }
    }

    companion object {
        data class AdminStakeListState(
            val result: UiState<List<StakeModel>> = UiState.Default
        )
    }
}
package com.mu.tote2024.presentation.screen.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.domain.usecase.gambler_usecase.GamblerUseCase
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.domain.usecase.prognosis_usecase.PrognosisUseCase
import com.mu.tote2024.domain.usecase.stake_usecase.StakeUseCase
import com.mu.tote2024.domain.usecase.team_usecase.TeamUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.Errors.ADD_GOAL_INCORRECT
import com.mu.tote2024.presentation.utils.Constants.GROUPS
import com.mu.tote2024.presentation.utils.Constants.GROUPS_COUNT
import com.mu.tote2024.presentation.utils.Constants.ID_NEW_GAME
import com.mu.tote2024.presentation.utils.Constants.KEY_ID
import com.mu.tote2024.presentation.utils.asTime
import com.mu.tote2024.presentation.utils.checkIsFieldEmpty
import com.mu.tote2024.presentation.utils.toLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameUseCase: GameUseCase,
    private val prognosisUseCase: PrognosisUseCase,
    private val stakeUseCase: StakeUseCase,
    teamUseCase: TeamUseCase,
    gamblerUseCase: GamblerUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state: MutableStateFlow<GameState> = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state

    private val _stateExit: MutableStateFlow<ExitState> = MutableStateFlow(ExitState())
    val stateExit: StateFlow<ExitState> = _stateExit

    //var isExit = false

    var gameId by mutableStateOf(savedStateHandle.get<String>(KEY_ID))
        private set

    private val isNewGame = gameId == ID_NEW_GAME

    var game by mutableStateOf(GameModel())
        private set

    var startTime = ""

    val teams = mutableListOf<String>()

    private var gamblersCount = 0

    var isExtraTime = false
        private set
    var isByPenalty = false
        private set

    var errorGameId = ""
        private set
    var errorStart = ""
        private set
    var errorGoal1 = ""
        private set
    var errorGoal2 = ""
        private set
    private var errorAddGoal1 = ""
    private var errorAddGoal2 = ""
    var errorExtraTime = ""
        private set

    var enabled = false
        private set

    init {
        viewModelScope.launch {
            gameUseCase.getGame(gameId ?: ID_NEW_GAME).collect { state ->
                val result = GameState(state).result
                if (result is UiState.Success) {
                    game = result.data

                    startTime = game.start.asTime()

                    enabled = checkValues()

                    /*teamUseCase.getTeamList().collect { stateTeam ->
                        if (stateTeam is UiState.Success) {
                            stateTeam.data.sortedBy { it.team }.forEach { team ->
                                teams.add(team.team)
                            }
                        }
                    }*/
                }

                _state.value = GameState(state)
            }
        }
        teamUseCase.getTeamList().onEach { stateTeam ->
            if (stateTeam is UiState.Success) {
                stateTeam.data.sortedBy { it.team }.forEach { team ->
                    teams.add(team.team)
                }
            }
        }.launchIn(viewModelScope)
        gamblerUseCase.getGamblerList().onEach { stateGambler ->
            if (stateGambler is UiState.Success) {
                gamblersCount = stateGambler.data.size
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.OnGameIdChange -> {
                game = game.copy(gameId = event.gameId)
                errorGameId = checkIsFieldEmpty(event.gameId)
            }

            is GameEvent.OnStartChange -> {
                game = game.copy(start = event.start)

                startTime = game.start.asTime()

                errorStart = checkIsFieldEmpty(event.start)
            }

            is GameEvent.OnGroupChange -> {
                game = game.copy(group = event.group)
            }

            is GameEvent.OnTeamChange -> {
                game = if (event.teamNo == 1)
                    game.copy(team1 = event.team)
                else
                    game.copy(team2 = event.team)
            }

            is GameEvent.OnGoalChange -> {
                checkGoal(
                    extraTime = event.extraTime,
                    teamNo = event.teamNo,
                    goal = event.goal
                )
            }

            is GameEvent.OnPenaltyChange -> {
                game = game.copy(penalty = event.team)
            }

            is GameEvent.OnCancel -> {
                _stateExit.value = ExitState(UiState.Success(true))
            }

            is GameEvent.OnSave -> {
                viewModelScope.launch {
                    gameUseCase.saveGame(game).collect { stateSave ->
                        if (stateSave is UiState.Success) {
                            prognosisUseCase.getPrognosis(game.gameId).collect{ prognosisState ->
                                if (prognosisState is UiState.Success) {
                                    val prognosis = prognosisState.data
                                    stakeUseCase.getStakeList().collect { stakesState ->
                                        if (stakesState is UiState.Success) {
                                            val stakes = stakesState.data.filter { it.gameId == game.gameId }
                                            stakes.forEach { stake ->
                                                val coefficient = when  {
                                                    stake.goal1.isBlank() -> prognosis.coefficientForFine
                                                    (stake.goal1 > stake.goal2) -> prognosis.coefficientForWin
                                                    (stake.goal1 < stake.goal2) -> prognosis.coefficientForDefeat
                                                    else -> prognosis.coefficientForDraw
                                                }

                                                val newStake = stake.copy(points = if (stake.goal1.isBlank()) {
                                                    prognosis.coefficientForFine
                                                } else {
                                                    calcPoints(stake, game, coefficient)
                                                })

                                                viewModelScope.launch {
                                                    stakeUseCase.saveStake(newStake).collect {
                                                        toLog("points: ${newStake.points}")
                                                    }
                                                }
                                            }
                                            _stateExit.value = ExitState(stateSave)
                                        }
                                    }
                                }
                            }
                        }
                        //_stateExit.value = ExitState(stateSave)
                    }
                }
            }
        }
        enabled = checkValues()
    }

    private fun calcPoints(stake: StakeModel, game: GameModel, coefficient: Double): Double =
        if (stake.goal1 == game.goal1 && stake.goal2 == game.goal2) {
            val points = coefficient * 2
            if (points <= gamblersCount) points else coefficient
        } else if (game.goal1 != game.goal2
            && (game.goal1.toInt() - game.goal2.toInt()) == (stake.goal1.toInt() - stake.goal2.toInt())
        ) {
            coefficient * 1.25
        } else if (
            (game.goal1 > game.goal2 && stake.goal1 > stake.goal2)
            || (game.goal1 == game.goal2 && stake.goal1 == stake.goal2)
            || (game.goal1 < game.goal2 && stake.goal1 < stake.goal2)
        ) {
            if (stake.goal1 == game.goal1 || stake.goal2 == game.goal2) {
                coefficient * 1.1
            } else {
                coefficient
            }
        } else if (stake.goal1 == game.goal1 || stake.goal2 == game.goal2) {
            0.15
        } else {
            0.0
        }

    private fun checkValues(): Boolean {
        isExtraTime = false
        isByPenalty = false

        var result = checkMainTime()

        if (isExtraTime) {
            result = result && checkExtraTime()
        }

        return result
    }

    private fun checkMainTime(): Boolean =
        if (game.gameId.isNotBlank()
            && game.start.isNotBlank()
            && game.group.isNotBlank()
            && game.team1.isNotBlank()
            && game.team2.isNotBlank()
        ) {
            if (isNewGame) {
                true
            } else {
                if (game.goal1.isNotBlank()
                    && game.goal2.isNotBlank()
                ) {
                    isExtraTime = (GROUPS.indexOf(game.group) >= GROUPS_COUNT
                            && game.goal1 == game.goal2)
                    if (!isExtraTime) {
                        game = game.copy(
                            addGoal1 = "",
                            addGoal2 = "",
                            penalty = "",
                        )
                    }
                    true
                } else {
                    false
                }
            }
        } else {
            false
        }

    private fun checkExtraTime(): Boolean {
        var result = if (game.addGoal1.isNotBlank())
            game.addGoal1 >= game.goal1
        else
            true

        result = result && if (game.addGoal2.isNotBlank())
            game.addGoal2 >= game.goal2
        else
            true

        isByPenalty = (game.addGoal1.isNotBlank() && game.addGoal1 >= game.goal1
                && game.addGoal2.isNotBlank() && game.addGoal2 >= game.goal2
                && game.addGoal1 == game.addGoal2)
        if (!isByPenalty) {
            game = game.copy(penalty = "")
        }
        return result
    }

    private fun checkGoal(extraTime: Boolean, teamNo: Int, goal: String) {
        if (!extraTime) {
            if (teamNo == 1) {
                game = game.copy(goal1 = goal)
                errorGoal1 = if (isNewGame) "" else checkIsFieldEmpty(goal)
            } else {
                game = game.copy(goal2 = goal)
                errorGoal2 = if (isNewGame) "" else checkIsFieldEmpty(goal)
            }
        } else {
            if (teamNo == 1) {
                game = game.copy(addGoal1 = goal)
                errorAddGoal1 = checkIsFieldEmpty(goal).ifBlank {
                    if (game.addGoal1 < game.goal1)
                        ADD_GOAL_INCORRECT
                    else ""
                }
            } else {
                game = game.copy(addGoal2 = goal)
                errorAddGoal2 = checkIsFieldEmpty(goal).ifBlank {
                    if (game.addGoal2 < game.goal2)
                        ADD_GOAL_INCORRECT
                    else ""
                }
            }
            errorExtraTime = errorAddGoal1.ifBlank { errorAddGoal2 }
        }
    }

    companion object {
        data class GameState(
            val result: UiState<GameModel> = UiState.Default,
        )

        data class ExitState(
            val result: UiState<Boolean> = UiState.Default,
        )
    }
}
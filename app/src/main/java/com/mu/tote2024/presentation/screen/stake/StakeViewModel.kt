package com.mu.tote2024.presentation.screen.stake

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.data.utils.Constants.CURRENT_ID
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_GAME_IS_NOT_FOUND
import com.mu.tote2024.domain.model.GameFlagsModel
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.model.PrognosisModel
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.domain.model.TeamModel
import com.mu.tote2024.domain.usecase.gambler_usecase.GamblerUseCase
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.domain.usecase.prognosis_usecase.PrognosisUseCase
import com.mu.tote2024.domain.usecase.stake_usecase.StakeUseCase
import com.mu.tote2024.domain.usecase.team_usecase.TeamUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.Errors.ADD_GOAL_INCORRECT
import com.mu.tote2024.presentation.utils.Constants.Errors.THE_GAME_IS_STARTED_YET
import com.mu.tote2024.presentation.utils.Constants.GROUPS
import com.mu.tote2024.presentation.utils.Constants.GROUPS_COUNT
import com.mu.tote2024.presentation.utils.Constants.KEY_ID
import com.mu.tote2024.presentation.utils.checkIsFieldEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StakeViewModel @Inject constructor(
    private val stakeUseCase: StakeUseCase,
    private val gameUseCase: GameUseCase,
    private val teamUseCase: TeamUseCase,
    private val gamblerUseCase: GamblerUseCase,
    private val prognosisUseCase: PrognosisUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(StakeState())
    val state: StateFlow<StakeState> = _state

    private val _stateExit: MutableStateFlow<ExitState> = MutableStateFlow(ExitState())
    val stateExit: StateFlow<ExitState> = _stateExit

    val gameId by mutableStateOf(savedStateHandle.get<String>(KEY_ID))

    var stake by mutableStateOf(StakeModel())
        private set
    var games by mutableStateOf(listOf<GameModel>())
        private set
    var start = ""
        private set
    var teams by mutableStateOf(listOf<TeamModel>())
        private set

    var flags = mutableListOf<GameFlagsModel>()
        private set

    var team1 = ""
        private set
    var team2 = ""
        private set
    var isExtraTime = false
        private set
    var isByPenalty = false
        private set

    private var errorGoal1 = ""
    private var errorGoal2 = ""
    private var errorAddGoal1 = ""
    private var errorAddGoal2 = ""

    var errorMainTime = ""
        private set
    var errorExtraTime = ""
        private set
    var errorByPenalty = ""
        private set

    var enabled = false
        private set

    init {
        viewModelScope.launch {
            teamUseCase.getTeamList().onEach { stateTeams ->
                if (stateTeams is UiState.Success) {
                    teams = stateTeams.data

                    stakeUseCase.getStake(gameId ?: "", CURRENT_ID).onEach { stateStake ->
                        when (val result = StakeState(stateStake).result) {
                            is UiState.Success -> {
                                gameUseCase.getGame(gameId ?: "").onEach { stateGame ->
                                    if (stateGame is UiState.Success) {
                                        start = stateGame.data.start

                                        stake = result.data
                                        //_state.value = StakeState(stateStake)

                                        team1 = stake.team1
                                        team2 = stake.team2

                                        addFlags(
                                            gameId = stake.gameId,
                                            team1 = team1,
                                            team2 = team2
                                        )
                                        enabled = checkValues()
                                    }
                                }.launchIn(viewModelScope)
                            }

                            is UiState.Error -> {
                                if (result.message == ERROR_GAME_IS_NOT_FOUND) {
                                    gameUseCase.getGame(gameId ?: "").onEach { stateGame ->
                                        if (stateGame is UiState.Success) {
                                            start = stateGame.data.start
                                            val game = stateGame.data
                                            stake = stake.copy(
                                                gameId = game.gameId,
                                                gamblerId = CURRENT_ID,
                                                //gamblerNick = GAMBLER.profile.nickname,
                                                group = game.group,
                                                team1 = game.team1,
                                                team2 = game.team2,
                                            )

                                            team1 = game.team1
                                            team2 = game.team2

                                            addFlags(
                                                gameId = game.gameId,
                                                team1 = team1,
                                                team2 = team2
                                            )
                                            enabled = checkValues()
                                        }
                                    }.launchIn(viewModelScope)
                                }
                            }

                            else -> {}
                        }

                        gameUseCase.getGameList().onEach { stateGame ->
                            if (stateGame is UiState.Success) {
                                games = stateGame.data
                                    .filter {
                                        (it.team1 in listOf(team1, team2) || it.team2 in listOf(team1, team2))
                                                && it.start.toLong() < System.currentTimeMillis()
                                                && it.gameId != gameId
                                    }
                                    .sortedBy { it.gameId }
                                games.forEach {
                                    addFlags(
                                        gameId = it.gameId,
                                        team1 = it.team1,
                                        team2 = it.team2
                                    )
                                }
                            }
                        }.launchIn(viewModelScope)
                        _state.value = StakeState(stateStake)
                    }.launchIn(viewModelScope)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onEvent(event: StakeEvent) {
        when (event) {
            is StakeEvent.OnGoalChange -> {
                checkGoal(
                    extraTime = event.extraTime,
                    teamNo = event.teamNo,
                    goal = event.goal
                )
            }

            is StakeEvent.OnPenaltyChange -> {
                stake = stake.copy(penalty = event.team)
            }

            is StakeEvent.OnCancel -> {
                _stateExit.value = ExitState(UiState.Success(true))
            }

            is StakeEvent.OnSave -> {
                if (start.toLong() < System.currentTimeMillis()) {
                    _state.value = StakeState(UiState.Error(THE_GAME_IS_STARTED_YET))
                } else {
                    viewModelScope.launch {
                        stakeUseCase.saveStake(stake).collect { stateSave ->
                            if (stateSave is UiState.Success) {
                                gamblerUseCase.getGamblerList().collect { gamblerListState ->
                                    if (gamblerListState is UiState.Success) {
                                        calcPrognosis(
                                            gameId = stake.gameId,
                                            gamblersCount = gamblerListState.data.size.toDouble()
                                        )
                                        _stateExit.value = ExitState(stateSave)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        enabled = checkValues()
    }

    private fun addFlags(
        gameId: String,
        team1: String,
        team2: String
    ) {
        val flag1 = teams.first { it.team == team1 }.flag
        val flag2 = teams.first { it.team == team2 }.flag
        flags.add(
            GameFlagsModel(
                gameId = gameId,
                flag1 = flag1,
                flag2 = flag2
            )
        )
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
        if (stake.goal1.isNotBlank()
            && stake.goal2.isNotBlank()
        ) {
            isExtraTime = (GROUPS.indexOf(stake.group) >= GROUPS_COUNT
                    && stake.goal1 == stake.goal2)
            if (!isExtraTime) {
                stake = stake.copy(
                    addGoal1 = "",
                    addGoal2 = "",
                    penalty = "",
                )
            }
            true
        } else {
            false
        }

    private fun checkExtraTime(): Boolean {
        var result = if (stake.addGoal1.isNotBlank())
            stake.addGoal1 >= stake.goal1
        else
            true

        result = result && if (stake.addGoal2.isNotBlank())
            stake.addGoal2 >= stake.goal2
        else
            true

        isByPenalty = (stake.addGoal1.isNotBlank() && stake.addGoal1 >= stake.goal1
                && stake.addGoal2.isNotBlank() && stake.addGoal2 >= stake.goal2
                && stake.addGoal1 == stake.addGoal2)
        //errorByPenalty = checkIsFieldEmpty(stake.penalty)

        if (isByPenalty) {
            result = result && stake.penalty.isNotBlank()
        } else {
            stake = stake.copy(penalty = "")
        }

        return result
    }

    private fun checkGoal(extraTime: Boolean, teamNo: Int, goal: String) {
        if (!extraTime) {
            if (teamNo == 1) {
                stake = stake.copy(goal1 = goal)
                errorGoal1 = checkIsFieldEmpty(goal)
            } else {
                stake = stake.copy(goal2 = goal)
                errorGoal2 = checkIsFieldEmpty(goal)
            }
            errorMainTime = errorGoal1.ifBlank { errorGoal2 }
        } else {
            if (teamNo == 1) {
                stake = stake.copy(addGoal1 = goal)
                errorAddGoal1 = checkIsFieldEmpty(goal).ifBlank {
                    if (stake.addGoal1 < stake.goal1)
                        ADD_GOAL_INCORRECT
                    else ""
                }
            } else {
                stake = stake.copy(addGoal2 = goal)
                errorAddGoal2 = checkIsFieldEmpty(goal).ifBlank {
                    if (stake.addGoal2 < stake.goal2)
                        ADD_GOAL_INCORRECT
                    else ""
                }
            }
            errorExtraTime = errorAddGoal1.ifBlank { errorAddGoal2 }
        }
    }

    private fun calcPrognosis(gameId: String, gamblersCount: Double) {
        viewModelScope.launch {
            stakeUseCase.getStakeList().collect { stateStakes ->
                if (stateStakes is UiState.Success) {
                    val stakes = stateStakes.data.filter { it.gameId == gameId }

                    val stakesWinCount =
                        stakes.filter { stake -> stake.goal1.isNotBlank() && stake.goal1 > stake.goal2 }.size
                    val stakesDrawCount =
                        stakes.filter { stake -> stake.goal1.isNotBlank() && stake.goal1 == stake.goal2 }.size
                    val stakesDefeatCount =
                        stakes.filter { stake -> stake.goal1.isNotBlank() && stake.goal1 < stake.goal2 }.size

                    val coefficientForWin = if (stakesWinCount > 0) gamblersCount / stakesWinCount else 0.0
                    val coefficientForDraw = if (stakesDrawCount > 0) gamblersCount / stakesDrawCount else 0.0
                    val coefficientForDefeat = if (stakesDefeatCount > 0) gamblersCount / stakesDefeatCount else 0.0
                    val coefficientForFine = -((coefficientForWin + coefficientForDraw + coefficientForDefeat) / 3.0)

                    prognosisUseCase.savePrognosis(
                        PrognosisModel(
                            gameId,
                            coefficientForWin,
                            coefficientForDraw,
                            coefficientForDefeat,
                            coefficientForFine,
                        )
                    ).collect {}
                }
            }
        }
    }

    companion object {
        data class StakeState(
            val result: UiState<StakeModel> = UiState.Default
        )

        data class ExitState(
            val result: UiState<Boolean> = UiState.Default,
        )
    }
}
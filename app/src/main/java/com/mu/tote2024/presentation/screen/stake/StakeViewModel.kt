package com.mu.tote2024.presentation.screen.stake

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.data.utils.Constants.CURRENT_ID
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_GAME_IS_NOT_FOUND
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.domain.usecase.stake_usecase.StakeUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.Errors.ADD_GOAL_INCORRECT
import com.mu.tote2024.presentation.utils.Constants.Errors.THE_GAME_IS_STARTED_YET
import com.mu.tote2024.presentation.utils.Constants.GROUPS
import com.mu.tote2024.presentation.utils.Constants.GROUPS_COUNT
import com.mu.tote2024.presentation.utils.Constants.KEY_ID
import com.mu.tote2024.presentation.utils.checkIsFieldEmpty
import com.mu.tote2024.presentation.utils.toLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StakeViewModel @Inject constructor(
    private val stakeUseCase: StakeUseCase,
    private val gameUseCase: GameUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(StakeState())
    val state: StateFlow<StakeState> = _state

    private val _stateExit: MutableStateFlow<ExitState> = MutableStateFlow(ExitState())
    val stateExit: StateFlow<ExitState> = _stateExit

    val gameId by mutableStateOf(savedStateHandle.get<String>(KEY_ID))
    var stake by mutableStateOf(StakeModel())
        private set
    var isExtraTime = false
        private set
    var isByPenalty = false
        private set
    private var errorGoal1 = ""
    private var errorGoal2 = ""
    var errorMainTime = ""
        private set

    private var errorAddGoal1 = ""
    private var errorAddGoal2 = ""
    var errorExtraTime = ""
        private set
    var errorByPenalty = ""
        private set
    var enabled = false
        private set

    init {
        viewModelScope.launch {
            stakeUseCase.getStake(gameId ?: "", CURRENT_ID).collect { stateStake ->
                when (val result = StakeState(stateStake).result) {
                    is UiState.Success -> {
                        stake = result.data
                        _state.value = StakeState(stateStake)

                        enabled = checkValues()
                    }

                    is UiState.Error -> {
                        if (result.message == ERROR_GAME_IS_NOT_FOUND) {
                            gameUseCase.getGame(gameId ?: "").collect { stateGame ->
                                if (stateGame is UiState.Success) {
                                    val game = stateGame.data
                                    stake = stake.copy(
                                        gameId = game.gameId,
                                        gamblerId = CURRENT_ID,
                                        start = game.start,
                                        group = game.group,
                                        team1 = game.team1,
                                        team2 = game.team2,
                                    )
                                    enabled = checkValues()
                                    _state.value = StakeState(UiState.Success(stake))
                                } else {
                                    _state.value = StakeState(UiState.Loading)
                                }
                            }
                        }
                    }

                    else -> {}
                }
            }
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
                if (stake.start.toLong() < System.currentTimeMillis()) {
                    _state.value = StakeState(UiState.Error(THE_GAME_IS_STARTED_YET))
                } else {
                    viewModelScope.launch {
                        stakeUseCase.saveStake(stake).collect { stateSave ->
                            _stateExit.value = ExitState(stateSave)
                        }
                    }
                }
            }
        }
        enabled = checkValues()
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
        errorByPenalty = checkIsFieldEmpty(stake.penalty)

        if (isByPenalty) {
            result = result && stake.penalty.isNotBlank()
        }

        return result
    }

    private fun checkGoal(extraTime: Boolean, teamNo: Int, goal: String) {
        toLog("extraTime: $extraTime, teamNo: $teamNo, goal: $goal")
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

    companion object {
        data class StakeState(
            val result: UiState<StakeModel> = UiState.Default
        )

        data class ExitState(
            val result: UiState<Boolean> = UiState.Default,
        )
    }
}
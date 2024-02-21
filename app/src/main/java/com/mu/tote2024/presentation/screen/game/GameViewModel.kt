package com.mu.tote2024.presentation.screen.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.domain.usecase.team_usecase.TeamUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.Errors.ADD_GOAL_INCORRECT
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
class GameViewModel @Inject constructor(
    private val gameUseCase: GameUseCase,
    private val teamUseCase: TeamUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state: MutableStateFlow<GameState> = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state

    var gameId by mutableStateOf(savedStateHandle.get<String>(KEY_ID))
        private set

    var game by mutableStateOf(GameModel())
        private set

    val teams = mutableListOf<String>()

    var isExtraTime = false
    var isByPenalty = false

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
            gameUseCase.getGame(gameId ?: "").collect { state ->
                val result = GameState(state).result
                toLog("result: $result")
                if (result is UiState.Success) {
                    game = result.data
                    enabled = checkValues()

                    teamUseCase.getTeamList().collect { stateTeam ->
                        if (stateTeam is UiState.Success) {
                            stateTeam.data.sortedBy { it.team }.forEach { team ->
                                teams.add(team.team)
                            }
                        }
                    }
                }

                _state.value = GameState(state)
            }
        }
    }

    fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.OnGameIdChange -> {
                game = game.copy(gameId = event.gameId)
                errorGameId = checkIsFieldEmpty(event.gameId)
            }

            is GameEvent.OnStartChange -> {
                game = game.copy(start = event.start)
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
                toLog("viewModel -> Cancel")
                _state.value = GameState(UiState.Success(game))
            }

            is GameEvent.OnSave -> {
                viewModelScope.launch {
                    gameUseCase.saveGame(game).collect { _ ->
                        _state.value = GameState(UiState.Success(game))
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
        if (game.goal1.isNotBlank()
            && game.goal2.isNotBlank()
        ) {
            isExtraTime = (GROUPS.indexOf(game.group) >= GROUPS_COUNT
                    && game.goal1 == game.goal2)

            (game.gameId.isNotBlank()
                    && game.start.isNotBlank()
                    && game.group.isNotBlank()
                    && game.team1.isNotBlank()
                    && game.team2.isNotBlank())
        } else {
            false
        }

    private fun checkExtraTime(): Boolean {
        val result = (game.addGoal1.isNotBlank()
                && game.addGoal2.isNotBlank()
                && game.addGoal1 >= game.goal1
                && game.addGoal2 >= game.goal2)
        isByPenalty = result && (game.addGoal1 == game.addGoal2)

        return result
    }

    private fun checkGoal(extraTime: Boolean, teamNo: Int, goal: String) {
        if (!extraTime) {
            if (teamNo == 1) {
                game = game.copy(goal1 = goal)
                errorGoal1 = checkIsFieldEmpty(goal)
            } else {
                game = game.copy(goal2 = goal)
                errorGoal2 = checkIsFieldEmpty(goal)
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
    }
}
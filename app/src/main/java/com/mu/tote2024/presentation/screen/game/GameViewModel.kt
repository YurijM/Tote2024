package com.mu.tote2024.presentation.screen.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.model.PrognosisModel
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
import com.mu.tote2024.presentation.utils.generateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameUseCase: GameUseCase,
    private val prognosisUseCase: PrognosisUseCase,
    private val stakeUseCase: StakeUseCase,
    teamUseCase: TeamUseCase,
    private val gamblerUseCase: GamblerUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state: MutableStateFlow<StateGame> = MutableStateFlow(StateGame())
    val state: StateFlow<StateGame> = _state

    var gameId by mutableStateOf(savedStateHandle.get<String>(KEY_ID))
        private set

    private val isNewGame = gameId == ID_NEW_GAME
    var game by mutableStateOf(GameModel())
        private set

    var startTime = "00:00"

    val teams = mutableListOf<String>()
    private var gamblersCount = 0
    var prognosis = PrognosisModel()
    private var stakes = mutableListOf<StakeModel>()
    private var games = mutableListOf<GameModel>()

    private var gamblers = mutableListOf<GamblerModel>()

    var isExtraTime = false
        private set
    var isByPenalty = false
        private set

    var result = mutableStateOf("")
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
        gameId?.let {
            gameUseCase.getGame(id = it).onEach { stateGame ->
                if (stateGame is UiState.Success) {
                    game = stateGame.data
                    startTime = game.start.asTime()

                    enabled = checkValues()
                }
            }.launchIn(viewModelScope)
        }
        gameUseCase.getGameList().onEach { stateGames ->
            if (stateGames is UiState.Success) {
                games = stateGames.data.toMutableList()
            }
        }.launchIn(viewModelScope)
        teamUseCase.getTeamList().onEach { stateTeam ->
            if (stateTeam is UiState.Success) {
                stateTeam.data.sortedBy { it.team }.forEach { team ->
                    teams.add(team.team)
                }
            }
        }.launchIn(viewModelScope)
        gameId?.let {
            prognosisUseCase.getPrognosis(gameId = it).onEach { statePrognosis ->
                if (statePrognosis is UiState.Success) {
                    prognosis = statePrognosis.data
                }
            }.launchIn(viewModelScope)
        }
        gamblerUseCase.getGamblerList().onEach { stateGambler ->
            if (stateGambler is UiState.Success) {
                gamblers = stateGambler.data.toMutableList()
                gamblersCount = stateGambler.data.size
            }
        }.launchIn(viewModelScope)
        stakeUseCase.getStakeList().onEach { stateStake ->
            if (stateStake is UiState.Success) {
                stakes = stateStake.data.toMutableList()
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
                _state.value = StateGame(UiState.Success(true))
            }

            is GameEvent.OnSave -> {
                gamblers.forEach { gambler ->
                    var stake = stakes.find { it.gameId == gameId && it.gamblerId == gambler.gamblerId }
                    if (stake == null) {
                        stake = StakeModel(
                            gameId = game.gameId,
                            gamblerId = gambler.gamblerId ?: "",
                            group = game.group,
                            team1 = game.team1,
                            team2 = game.team2
                        )
                        stakes.add(stake)
                    }
                    val coefficient = when {
                        stake.goal1.isBlank() -> prognosis.coefficientForFine
                        (stake.goal1 > stake.goal2) -> prognosis.coefficientForWin
                        (stake.goal1 < stake.goal2) -> prognosis.coefficientForDefeat
                        else -> prognosis.coefficientForDraw
                    }

                    val points = if (stake.goal1.isBlank()) {
                        prognosis.coefficientForFine
                    } else {
                        calcStakePoints(stake, game, coefficient)
                    }

                    val index = stakes.indexOf(stake)
                    stakes[index] = stake.copy(points = points)

                    stakeUseCase.saveStake(
                        stake.copy(points = points)
                    ).launchIn(viewModelScope)
                }

                gameUseCase.saveGame(game).onEach { stateSave ->
                    _state.value = StateGame(stateSave)
                    if (stateSave is UiState.Success) {
                        //if ((gameId?.toInt() ?: 0) > 0) {
                        if (game.goal1.isNotBlank() && game.goal2.isNotBlank()) {
                            val index = games.indexOf(games.find { it.gameId == gameId })
                            games[index] = game.copy()

                            saveStakePlace()

                            calcGamblerPointsPrev()
                            calcGamblerPlacePrev()
                            calcGamblerPoints()
                            calcGamblerPlace()

                            saveGamblerResult()
                        }
                    }
                }.launchIn(viewModelScope)
            }

            is GameEvent.OnGenerateResult -> {
                result.value = generateResult()
            }
        }
        enabled = checkValues()
    }

    private fun saveGamblerResult() {
        gamblers.forEach { gambler ->
            gamblerUseCase.saveGambler(gambler).launchIn(viewModelScope)
        }
    }

    private fun calcGamblerPoints() {
        val gamesPlayed = games.filter { it.start.toLong() <= System.currentTimeMillis() }
        if (gamesPlayed.isNotEmpty()) {
            gamblers.forEachIndexed { index, gambler ->
                val points = stakes.filter { it.gamblerId == gambler.gamblerId }.sumOf { it.points }
                val result = gambler.result.copy(points = points)
                gamblers[index] = gambler.copy(result = result)
            }
        }
    }

    private fun calcGamblerPlace() {
        val gamesPlayed = games.filter { it.start.toLong() <= System.currentTimeMillis() }
        if (gamesPlayed.isNotEmpty()) {
            val gamblersSortedByPoints = gamblers.sortedWith(
                compareByDescending<GamblerModel> { item -> item.result.points }
                    .thenBy { item -> item.profile.nickname }
            )
            var points = 0.0
            var place = 1
            var step = 0

            gamblersSortedByPoints.forEach { gambler ->
                if (points == gambler.result.points) {
                    step++
                } else {
                    place += step
                    points = gambler.result.points

                    step = 1
                }

                val index = gamblers.indexOf(gamblers.find { it.gamblerId == gambler.gamblerId })
                val result = gambler.result.copy(place = place)

                gamblers[index] = gambler.copy(result = result)
            }
        }
    }

    private fun calcGamblerPlacePrev() {
        val gamesPlayed = games.filter { it.start.toLong() <= System.currentTimeMillis() && it.gameId != gameId }
        val gamblersSortedByPointsPrev = gamblers.sortedByDescending { item -> item.result.pointsPrev }

        if (gamesPlayed.isNotEmpty()) {
            var points = 0.0
            var place = 1
            var step = 0

            gamblersSortedByPointsPrev.forEach { gambler ->
                if (points == gambler.result.pointsPrev) {
                    step++
                } else {
                    place += step
                    points = gambler.result.pointsPrev

                    step = 1
                }

                val result = gambler.result.copy(placePrev = place)
                val index = gamblers.indexOf(gamblers.find { it.gamblerId == gambler.gamblerId })

                gamblers[index] = gambler.copy(result = result)
            }
        } /*else {
            gamblersSortedByPointsPrev.forEach { gambler ->
                val place = gambler.result.place
                val result = gambler.result.copy(placePrev = place)
                val index = gamblers.indexOf(gamblers.find { it.gamblerId == gambler.gamblerId })

                gamblers[index] = gambler.copy(result = result)
            }
        }*/
    }

    private fun calcGamblerPointsPrev() {
        val gameIds = mutableListOf<Int>()
        val currentGame = games.find { it.gameId == gameId }
        //games.filter { it.start.toLong() <= System.currentTimeMillis() && it.gameId != gameId }.forEach { game ->
        games.filter {
            it.start.toLong() <= System.currentTimeMillis()
                    && it.start.toLong() < (currentGame?.start?.toLong() ?: 0)
        }.forEach { game ->
            gameIds.add(game.gameId.toInt())
        }

        gamblers.forEach { gambler ->
            val pointsPrev = stakes
                .filter { it.gamblerId == gambler.gamblerId && it.gameId.toInt() in gameIds }
                .sumOf { it.points }
            val result = gambler.result.copy(pointsPrev = pointsPrev)
            val index = gamblers.indexOf(gamblers.find { it.gamblerId == gambler.gamblerId })

            gamblers[index] = gambler.copy(result = result)
        } /*else {
            gamblers.forEach { gambler ->
                val points = gambler.result.points
                val result = gambler.result.copy(pointsPrev = points)
                val index = gamblers.indexOf(gamblers.find { it.gamblerId == gambler.gamblerId })

                gamblers[index] = gambler.copy(result = result)
            }
        }*/
    }

    private fun saveStakePlace() {
        var place = 1
        var points = 0.0
        var step = 0

        stakes.filter { it.gameId == gameId }.sortedWith(
            compareByDescending<StakeModel> { item -> item.points }
                .thenBy { el -> el.gamblerId }
        ).forEach { stake ->
            if (points == stake.points) {
                step++
            } else {
                place += step
                points = stake.points

                step = 1
            }
            stakeUseCase.saveStake(
                stake.copy(place = place)
            ).launchIn(viewModelScope)

            val index = stakes.indexOf(stake)
            stakes[index] = stake.copy(place = place)
        }
    }

    private fun calcStakePointsAddTime(stake: StakeModel, game: GameModel): Double {
        var points = if (stake.goal1 == game.goal1 && stake.goal2 == game.goal2
            && stake.addGoal1 == game.addGoal1 && stake.addGoal2 == game.addGoal2
        ) {
            2.0
        } else if (game.addGoal1 != game.addGoal2
            && (stake.addGoal1.toInt() - stake.addGoal2.toInt()) == (game.addGoal1.toInt() - game.addGoal2.toInt())
        ) {
            1.25
        } else if (
            (game.addGoal1 > game.addGoal2 && stake.addGoal1 > stake.addGoal2)
            || (game.addGoal1 == game.addGoal2 && stake.addGoal1 == stake.addGoal2)
            || (game.addGoal1 < game.addGoal2 && stake.addGoal1 < stake.addGoal2)
        ) {
            1.0
        } else if (stake.addGoal1 == game.addGoal1 || stake.addGoal2 == game.addGoal2) {
            0.1
        } else {
            0.0
        }

        if (game.penalty.isNotBlank() && stake.penalty == game.penalty) {
            points += 1
        }

        return points
    }

    private fun calcStakePoints(stake: StakeModel, game: GameModel, coefficient: Double): Double {
        var result = if (stake.goal1 == game.goal1 && stake.goal2 == game.goal2) {
            val points = coefficient * 2
            if (points <= gamblersCount) points else gamblersCount * 1.1
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

        if (game.addGoal1.isNotBlank() && game.addGoal2.isNotBlank()
            && stake.addGoal1.isNotBlank() && stake.addGoal2.isNotBlank()
        ) {
            result += calcStakePointsAddTime(stake, game)
        }

        return result
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
                    true //false
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
        data class StateGame(
            val result: UiState<Boolean> = UiState.Default,
        )
    }
}
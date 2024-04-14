package com.mu.tote2024.presentation.screen.prognosis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.model.PrognosisModel
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.domain.usecase.prognosis_usecase.PrognosisUseCase
import com.mu.tote2024.domain.usecase.stake_usecase.StakeUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PrognosisListViewModel @Inject constructor(
    gameUseCase: GameUseCase,
    private val stakeUseCase: StakeUseCase,
    private val prognosisUseCase: PrognosisUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<GameState> = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state

    /*var games = listOf<GameModel>()
        private set
    val gameIds = arrayListOf<String>()*/

    var stakes = listOf<StakeModel>()
        private set

    var prognosis = listOf<PrognosisModel>()
        private set

    init {
        gameUseCase.getGameList().onEach { stateGame ->
            if (stateGame is UiState.Success) {
                stakeUseCase.getStakeList().onEach { stateStake ->
                    if (stateStake is UiState.Success) {
                        stakes = stateStake.data
                        prognosisUseCase.getPrognosisList().onEach { statePrognosis ->
                            if (statePrognosis is UiState.Success) {
                                prognosis = statePrognosis.data
                                _state.value = GameState(stateGame)
                            }
                        }.launchIn(viewModelScope)
                    }
                }.launchIn(viewModelScope)
            } else {
                _state.value = GameState(stateGame)
            }
        }.launchIn(viewModelScope)



        /*viewModelScope.launch {
            gameUseCase.getGameList().collect { stateGame ->
                _state.value = PrognosisListState(UiState.Loading)

                if (stateGame is UiState.Success) {
                    games = stateGame.data.filter { it.start.toDouble() < System.currentTimeMillis() }
                    games.forEach { game ->
                        gameIds.add(game.gameId)
                    }

                    stakeUseCase.getStakeList().collect { stateStake ->
                        if (stateStake is UiState.Success) {
                            stakes = stateStake.data.filter { it.gameId in gameIds }
                                .sortedWith(
                                    compareBy<StakeModel> { it.gameId }
                                        .thenBy { it.gamblerNick }
                                )

                            prognosisUseCase.getPrognosisList().collect { statePrognosis ->
                                _state.value = PrognosisListState(statePrognosis)
                            }
                        }
                    }
                }
            }
        }*/
    }

    companion object {
        /*data class PrognosisListState(
            val result: UiState<List<PrognosisModel>> = UiState.Default
        )*/
        data class GameState(
            val result: UiState<List<GameModel>> = UiState.Default
        )
    }
}
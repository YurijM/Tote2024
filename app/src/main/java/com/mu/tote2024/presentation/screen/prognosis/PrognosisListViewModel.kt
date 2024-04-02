package com.mu.tote2024.presentation.screen.prognosis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.model.PrognosisModel
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.domain.model.TeamModel
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.domain.usecase.prognosis_usecase.PrognosisUseCase
import com.mu.tote2024.domain.usecase.stake_usecase.StakeUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrognosisListViewModel @Inject constructor(
    private val gameUseCase: GameUseCase,
    private val stakeUseCase: StakeUseCase,
    private val prognosisUseCase: PrognosisUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<PrognosisListState> = MutableStateFlow(PrognosisListState())
    val state: StateFlow<PrognosisListState> = _state

    var games = listOf<GameModel>()
        private set
    val gameIds = arrayListOf<String>()
    var stakes = listOf<StakeModel>()
        private set

    init {
        viewModelScope.launch {
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
        }
    }

    companion object {
        data class PrognosisListState(
            val result: UiState<List<PrognosisModel>> = UiState.Default
        )
    }
}
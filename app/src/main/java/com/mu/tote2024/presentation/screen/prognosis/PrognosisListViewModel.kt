package com.mu.tote2024.presentation.screen.prognosis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.model.PrognosisModel
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.domain.usecase.gambler_usecase.GamblerUseCase
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
    private val gamblerUseCase: GamblerUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<GameState> = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state

    var gamblers = listOf<GamblerModel>()
        private set

    var stakes = listOf<StakeModel>()
        private set

    var prognosis = listOf<PrognosisModel>()
        private set

    init {
        gameUseCase.getGameList().onEach { stateGame ->
            if (stateGame is UiState.Success) {
                gamblerUseCase.getGamblerList().onEach { stateGambler ->
                    if (stateGambler is UiState.Success) {
                        gamblers = stateGambler.data
                    }
                }.launchIn(viewModelScope)
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
    }

    companion object {
        data class GameState(
            val result: UiState<List<GameModel>> = UiState.Default
        )
    }
}
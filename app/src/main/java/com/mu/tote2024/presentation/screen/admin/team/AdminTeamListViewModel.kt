package com.mu.tote2024.presentation.screen.admin.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.TeamModel
import com.mu.tote2024.domain.usecase.team_usecase.TeamUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminTeamListViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<AdminTeamListState> = MutableStateFlow(AdminTeamListState())
    val state: StateFlow<AdminTeamListState> = _state

    init {
        viewModelScope.launch {
            teamUseCase.getTeamList().collect {
                _state.value = AdminTeamListState(it)
            }
        }
    }

    fun onEvent(event: AdminTeamListEvent) {
        when (event) {
            is AdminTeamListEvent.OnLoad -> {
                teams.forEach { team ->
                    if (team.team.isNotBlank()) {
                        viewModelScope.launch {
                            teamUseCase.saveTeam(team).collect {}
                        }
                    }
                }
            }
        }
    }

    companion object {
        data class AdminTeamListState(
            val result: UiState<List<TeamModel>> = UiState.Default
        )

        val teams = listOf(
            TeamModel(
                group = "A",
                itemNo = 1,
                team = "Германия",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Fger.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "A",
                itemNo = 2,
                team = "Шотландия",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Fsco.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "A",
                itemNo = 3,
                team = "Венгрия",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Fhun.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "A",
                itemNo = 4,
                team = "Швейцария",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Fsui.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "B",
                itemNo = 1,
                team = "Испания",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Fesp.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "B",
                itemNo = 2,
                team = "Хорватия",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Fcro.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "B",
                itemNo = 3,
                team = "Италия",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Fita.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "B",
                itemNo = 4,
                team = "Албания",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Falb.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "C",
                itemNo = 1,
                team = "Словения",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Fsvn.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "C",
                itemNo = 2,
                team = "Дания",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Fden.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "C",
                itemNo = 3,
                team = "Сербия",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Fsrb.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "C",
                itemNo = 4,
                team = "Англия",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Feng.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "D",
                itemNo = 1,
                team = "",
                flag = ""
            ),
            TeamModel(
                group = "D",
                itemNo = 2,
                team = "Нидерланды",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Fned.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "D",
                itemNo = 3,
                team = "Австрия",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Faut.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "D",
                itemNo = 4,
                team = "Франция",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Ffra.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "E",
                itemNo = 1,
                team = "Бельгия",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Fbel.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "E",
                itemNo = 2,
                team = "Словакия",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Fsvk.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "E",
                itemNo = 3,
                team = "Румыния",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Frou.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "E",
                itemNo = 4,
                team = "",
                flag = ""
            ),
            TeamModel(
                group = "F",
                itemNo = 1,
                team = "Турция",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Ftur.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "F",
                itemNo = 2,
                team = "",
                flag = ""
            ),
            TeamModel(
                group = "F",
                itemNo = 3,
                team = "Португалия",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Fpor.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            ),
            TeamModel(
                group = "F",
                itemNo = 4,
                team = "Чехия",
                flag = "https://firebasestorage.googleapis.com/v0/b/tote-2024-e3d04.appspot.com/o/flags%2Fcze.png?alt=media&token=f35cb273-26fd-400f-ae01-081e200f6951"
            )
        )
    }
}
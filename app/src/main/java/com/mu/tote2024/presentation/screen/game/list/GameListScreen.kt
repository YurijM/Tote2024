package com.mu.tote2024.presentation.screen.game.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.model.TeamModel
import com.mu.tote2024.presentation.ui.common.UiState

@Composable
fun GameListScreen(
    viewModel: GameListViewModel = hiltViewModel()
) {
    val isLoading = remember { mutableStateOf(false) }
    var listGame by remember { mutableStateOf<List<GameModel>>(listOf()) }
    var listTeam by remember { mutableStateOf<List<TeamModel>>(listOf()) }

    val state by viewModel.state.collectAsState()

    when (val result = state.result) {
        is UiState.Loading -> {
            isLoading.value = true
        }

        is UiState.Success -> {
            isLoading.value = false

            listTeam = viewModel.listTeam
                .sortedWith(
                    compareBy<TeamModel> { it.group }
                        .thenBy { it.itemNo }
                )

            listGame = result.data
        }

        is UiState.Error -> {
            isLoading.value = false
        }

        else -> {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        var group = ""
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(listTeam) { team ->
                if (group != team.group) {
                    group = team.group
                    Text(text = team.group)
                }
                Text(text = "${team.itemNo} ${team.team}")
            }
        }
    }
}
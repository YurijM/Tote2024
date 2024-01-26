package com.mu.tote2024.presentation.screen.admin.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.domain.model.TeamModel
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.OkAndCancel
import com.mu.tote2024.presentation.components.Title
import com.mu.tote2024.presentation.ui.common.UiState

@Composable
fun AdminTeamListScreen(
    viewModel: AdminTeamListViewModel = hiltViewModel()
) {
    val isLoading = remember { mutableStateOf(false) }
    var teamList by remember { mutableStateOf<List<TeamModel>>(listOf()) }

    val state by viewModel.state.collectAsState()

    when (val result = state.result) {
        is UiState.Loading -> {
            isLoading.value = true
        }

        is UiState.Success -> {
            isLoading.value = false

            teamList = result.data
                .sortedBy { it.team }
        }

        is UiState.Error -> {
            isLoading.value = false
        }

        else -> {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp)
    ) {
        Title(R.string.admin_team_list)
        OkAndCancel(
            titleOk = R.string.load,
            enabledOk = true,
            showCancel = false,
            onSave = { viewModel.onEvent(AdminTeamListEvent.OnLoad) },
            onCancel = {}
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp)
        ) {
            items(teamList) { team ->
                AdminTeamItemScreen(team = team)
            }
        }
    }

    if (isLoading.value) {
        AppProgressBar()
    }
}


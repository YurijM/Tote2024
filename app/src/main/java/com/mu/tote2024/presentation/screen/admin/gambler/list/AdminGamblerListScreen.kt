package com.mu.tote2024.presentation.screen.admin.gambler.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.presentation.ui.common.UiState

@Composable
fun AdminGamblerListScreen(
    viewModel: AdminGamblerListViewModel = hiltViewModel(),
    toGambler: (id: String) -> Unit
) {
    val isLoading = remember { mutableStateOf(false) }
    var gamblers by remember { mutableStateOf<List<GamblerModel>>(listOf()) }

    val state by viewModel.state.collectAsState()

    when (val result = state.result) {
        is UiState.Loading -> {
            isLoading.value = true
        }

        is UiState.Success -> {
            isLoading.value = false

            gamblers = result.data
                .sortedBy { it.profile.nickname }
        }

        is UiState.Error -> {
            isLoading.value = false
        }

        else -> {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Text(
            text = stringResource(id = R.string.admin_gambler_list),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()

        ) {
            items(gamblers) { gambler ->
                AdminGamblerItemScreen(
                    gambler = gambler,
                    onClick = { gambler.gamblerId?.let { toGambler(it) } }
                )
            }
        }

        if (isLoading.value) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
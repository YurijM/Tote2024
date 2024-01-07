package com.mu.tote2024.presentation.screen.rating

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.presentation.ui.common.UiState

@Composable
fun RatingScreen(
    viewModel: RatingViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
    var gamblers by remember { mutableStateOf<List<GamblerModel>>(listOf()) }

    val state by viewModel.state.collectAsState()

    when (val result = state.result) {
        is UiState.Loading -> {
            isLoading = true
        }

        is UiState.Success -> {
            isLoading = false
            gamblers = result.data.filter { it.rate > 0 }
        }

        is UiState.Error -> {
            isLoading = false
        }

        else -> {}
    }

    LazyColumn() {
        items(gamblers) { gambler ->
            Text(text = gambler.profile?.nickname ?: "")
        }
    }

    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
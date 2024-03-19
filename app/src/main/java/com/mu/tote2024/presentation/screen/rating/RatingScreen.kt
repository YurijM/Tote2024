package com.mu.tote2024.presentation.screen.rating

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.data.utils.Constants.GAMBLER
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.ui.common.UiState

@Composable
fun RatingScreen(
    viewModel: RatingViewModel = hiltViewModel(),
    toAdminGamblerPhoto: (String) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var gamblers by remember { mutableStateOf<List<GamblerModel>>(listOf()) }
    var rate by remember { mutableIntStateOf(1) }
    var profileIsValid by remember { mutableStateOf(false) }

    val state by viewModel.state.collectAsState()
    val result = state.result

    LaunchedEffect(key1 = result) {
        when (result) {
            is UiState.Loading -> {
                isLoading = true
            }

            is UiState.Success -> {
                isLoading = false
                gamblers = result.data
                    .filter { it.rate > 0 }
                    //.sortedWith(compareBy { it.result?.points ?: 0.00 })
                    .sortedWith(
                        compareByDescending<GamblerModel> { it.result.points }
                            .thenBy { it.profile.nickname }
                    )
                rate = GAMBLER.rate
                profileIsValid = viewModel.checkProfile()
            }

            is UiState.Error -> {
                isLoading = false
            }

            else -> {}
        }
    }

    Column {
        if (profileIsValid && rate == 0) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline
                )
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = "Так как Вы ещё не перечислили свою ставку, " +
                            "то Вам пока доступен только просмотр списка " +
                            "уже зарегистрированных участников",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        LazyColumn {
            items(gamblers) { gambler ->
                RatingItemScreen(
                    nickname = gambler.profile.nickname,
                    photoUrl = gambler.profile.photoUrl,
                    points = gambler.result.points,
                    toAdminGamblerPhoto = { toAdminGamblerPhoto(gambler.profile.photoUrl) }
                )
            }
        }
    }

    if (isLoading) {
        AppProgressBar()
    }
}
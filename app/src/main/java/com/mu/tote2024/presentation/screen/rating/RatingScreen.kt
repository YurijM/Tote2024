package com.mu.tote2024.presentation.screen.rating

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.data.utils.Constants.GAMBLER
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.AppTournamentIsFinished
import com.mu.tote2024.presentation.ui.ColorFemale
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.FEMALE
import com.mu.tote2024.presentation.utils.Constants.MALE
import java.text.DecimalFormat

@Composable
fun RatingScreen(
    viewModel: RatingViewModel = hiltViewModel(),
    toAdminGamblerPhoto: (String) -> Unit,
    toChart: (String) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var gamblers by remember { mutableStateOf<List<GamblerModel>>(listOf()) }
    var maleResult by remember { mutableDoubleStateOf(0.0) }
    var femaleResult by remember { mutableDoubleStateOf(0.0) }
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
                    .sortedWith(
                        compareByDescending<GamblerModel> { it.result.points }
                            .thenBy { it.profile.nickname }
                    )
                val male = gamblers.filter { it.profile.gender == MALE }
                val female = gamblers.filter { it.profile.gender == FEMALE }

                maleResult = male.sumOf { it.result.points } / male.size.toDouble()
                femaleResult = female.sumOf { it.result.points } / male.size.toDouble()

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

        if (viewModel.finish.finish)
            AppTournamentIsFinished(text = viewModel.finish.text)

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            items(viewModel.winners) { winner ->
                val winnings = viewModel.winningsList.find { it.gamblerId == winner.gamblerId }?.winnings ?: 0
                RatingWinItemScreen(winner, winnings)
            }
        }

        if (!isLoading)
            GenderTournament(maleResult, femaleResult)

        LazyColumn {
            items(gamblers) { gambler ->
                RatingItemScreen(
                    nickname = gambler.profile.nickname,
                    photoUrl = gambler.profile.photoUrl,
                    points = gambler.result.points,
                    prevPoints = gambler.result.pointsPrev,
                    place = gambler.result.place,
                    prevPlace = gambler.result.placePrev,
                    showPrev = viewModel.showArrows,
                    toAdminGamblerPhoto = { toAdminGamblerPhoto(gambler.profile.photoUrl) },
                    toChart = { if (gambler.gamblerId != null) toChart(gambler.gamblerId) }
                )
            }
        }
    }

    if (isLoading) {
        AppProgressBar()
    }
}

@Composable
fun GenderTournament(maleResult: Double, femaleResult: Double) {
    Row(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 4.dp),
                text = "М",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Card(
                modifier = Modifier.width(64.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentColor = Color.White
                )
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    text = DecimalFormat("0.00").format(maleResult),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier.width(64.dp),
                colors = CardDefaults.cardColors(
                    containerColor = ColorFemale,
                    contentColor = Color.White
                )
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    text = DecimalFormat("0.00").format(femaleResult),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            }
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "Ж",
                color = ColorFemale,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

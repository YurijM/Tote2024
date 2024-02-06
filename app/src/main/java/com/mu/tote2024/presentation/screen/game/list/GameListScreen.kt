package com.mu.tote2024.presentation.screen.game.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.model.TeamModel
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.EMPTY
import com.mu.tote2024.presentation.utils.Constants.GROUPS_COUNT
import com.mu.tote2024.presentation.utils.toLog

@Composable
fun GameListScreen(
    viewModel: GameListViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
    var listGame by remember { mutableStateOf<List<GameModel>>(listOf()) }
    var listTeam by remember { mutableStateOf<List<TeamModel>>(listOf()) }

    val state by viewModel.state.collectAsState()

    when (val result = state.result) {
        is UiState.Loading -> {
            isLoading = true
        }

        is UiState.Success -> {
            isLoading = false

            listTeam = viewModel.listTeam
                .sortedWith(
                    compareBy<TeamModel> { it.group }
                        .thenBy { it.itemNo }
                )

            listGame = result.data
        }

        is UiState.Error -> {
            isLoading = false
        }

        else -> {}
    }

    if (listGame.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            (1..GROUPS_COUNT).forEach { index ->
                val group = listTeam[(index - 1) * 4].group
                val list = listTeam.filter { team -> team.group == group }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "группа $group",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
                toLog("group $group games: ${listGame.filter { game -> game.group == group }}")
                Games_Table(listTeam = list)
            }
        }
    }

    if (isLoading) {
        AppProgressBar()
    }
}

/**
 * The horizontally scrollable table with header and content.
 * @param columnCount the count of columns in the table
 * @param cellWidth the width of column, can be configured based on index of the column.
 * @param data the data to populate table.
 * @param modifier the modifier to apply to this layout node.
 * @param headerCellContent a block which describes the header cell content.
 * @param cellContent a block which describes the cell content.
 */
@Composable
fun <T> Table(
    columnCount: Int,
    cellWidth: (index: Int) -> Dp,
    data: List<T>,
    modifier: Modifier = Modifier,
    headerCellContent: @Composable (index: Int) -> Unit,
    cellContent: @Composable (index: Int, item: T) -> Unit,
) {
    Surface(
        modifier = modifier
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            items((0 until columnCount).toList()) { columnIndex ->
                Column {
                    (0..data.size).forEach { index ->
                        Surface(
                            border = BorderStroke(Dp.Hairline, Color.LightGray),
                            contentColor = Color.Transparent,
                            modifier = Modifier.width(cellWidth(columnIndex))
                        ) {
                            if (index == 0) {
                                headerCellContent(columnIndex)
                            } else {
                                cellContent(columnIndex, data[index - 1])
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Games_Table(listTeam: List<TeamModel>) {
    val cellWidth: (Int) -> Dp = { index ->
        when (index) {
            0 -> 112.dp
            1, 2, 3, 4 -> 40.dp
            5, 6, 7, 10 -> 32.dp
            8 -> 56.dp
            9 -> 36.dp
            else -> 0.dp
        }
    }
    val headerCellTitle: @Composable (Int) -> Unit = { index ->
        val value = when (index) {
            0 -> "Команда"
            1, 2, 3, 4 -> index.toString()
            5 -> "В"
            6 -> "Н"
            7 -> "П"
            8 -> "Мячи"
            9 -> "О"
            10 -> "М"
            else -> ""
        }

        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface,
            style = TextStyle(fontSize = MaterialTheme.typography.titleSmall.fontSize),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Black,
            //extDecoration = TextDecoration.Underline
        )
    }

    val cellText: @Composable (Int, TeamModel) -> Unit = { index, item ->
        val value = when (index) {
            0 -> item.team
            1, 2, 3, 4 -> if (index == item.itemNo) EMPTY else "${(0..9).random()}:${(0..9).random()}"
            5, 6, 7, 10 -> "1"
            8 -> "12:10"
            9 -> "15"
            else -> ""
        }

        if (value == EMPTY) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onSurface)
            ) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = ""
                )
            }
        } else {
            Text(
                text = value, //if (value == "empty") "" else value,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = if (value.isEmpty() || value[0].isDigit()) TextAlign.Center else TextAlign.Start,
                modifier = Modifier.padding(4.dp),
                maxLines = 1,
                overflow = if (value.isEmpty() || value[0].isDigit()) TextOverflow.Clip else TextOverflow.Ellipsis
            )
        }
    }

    Table(
        columnCount = 11,
        cellWidth = cellWidth,
        data = listTeam,
        modifier = Modifier.padding(top = 4.dp),
        headerCellContent = headerCellTitle,
        cellContent = cellText
    )
}
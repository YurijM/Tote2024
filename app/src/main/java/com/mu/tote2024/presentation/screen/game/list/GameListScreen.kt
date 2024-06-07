package com.mu.tote2024.presentation.screen.game.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.data.utils.Constants.GAMBLER
import com.mu.tote2024.domain.model.GameFlagsModel
import com.mu.tote2024.domain.model.GroupTeamResultModel
import com.mu.tote2024.presentation.components.AppFabAdd
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.screen.game.list_group.GroupGameItemScreen
import com.mu.tote2024.presentation.ui.ColorDefeat
import com.mu.tote2024.presentation.ui.ColorDraw
import com.mu.tote2024.presentation.ui.ColorWin
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.EMPTY
import com.mu.tote2024.presentation.utils.Constants.GROUPS
import com.mu.tote2024.presentation.utils.Constants.GROUPS_COUNT
import com.mu.tote2024.presentation.utils.Constants.GROUP_N
import com.mu.tote2024.presentation.utils.Constants.ID_NEW_GAME
import com.mu.tote2024.presentation.utils.withParam

@Composable
fun GameListScreen(
    viewModel: GameListViewModel = hiltViewModel(),
    toGroupGameList: (String) -> Unit,
    toGame: (String) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }

    var result by remember { mutableStateOf<List<GroupTeamResultModel>>(listOf()) }

    val state by viewModel.state.collectAsState()

    when (val data = state.result) {
        is UiState.Loading -> {
            isLoading = true
        }

        is UiState.Success -> {
            isLoading = false

            result = data.data
        }

        is UiState.Error -> {
            isLoading = false
        }

        else -> {}
    }

    if (result.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            (GROUPS.size - 1 downTo GROUPS_COUNT).forEach { index ->
                val list = viewModel.games
                    .filter { it.group == GROUPS[index] }
                    .sortedBy { it.gameId }

                if (list.isNotEmpty()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = GROUPS[index],
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )

                    list.forEach { game ->
                        Box(
                            modifier = Modifier.padding(
                                start = 8.dp,
                                end = 8.dp,
                                bottom = 8.dp
                            )
                        ) {
                            GroupGameItemScreen(
                                game = game,
                                flagList = if (viewModel.flags.isNotEmpty()) {
                                    viewModel.flags.first { flag -> flag.gameId == game.gameId }
                                } else {
                                    GameFlagsModel()
                                },
                                onClick = { if (GAMBLER.admin) toGame(game.gameId) }
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 4.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(bottom = 48.dp)
            ) {
                (1..GROUPS_COUNT).forEach { index ->
                    val group = GROUPS[index - 1]
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = GROUP_N.withParam(group),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Games_Table(
                        result = result.filter { it.group == group },
                        onClick = { if (GAMBLER.admin) toGroupGameList(group) }
                    )
                }
            }
        }

        if (GAMBLER.admin) {
            AppFabAdd(
                onAdd = { toGame(ID_NEW_GAME) }
            )
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
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clickable(
                //role = Role.Button,
                onClick = { onClick() }
            ),
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
                            border = BorderStroke(Dp.Hairline, MaterialTheme.colorScheme.secondaryContainer),
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
fun Games_Table(
    result: List<GroupTeamResultModel>,
    onClick: () -> Unit
) {
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
            0 -> stringResource(id = R.string.team)
            1, 2, 3, 4 -> index.toString()
            5 -> "В"
            6 -> "Н"
            7 -> "П"
            8 -> stringResource(R.string.balls)
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
            fontWeight = FontWeight.Black
        )
    }

    val cellText: @Composable (Int, GroupTeamResultModel) -> Unit = { index, item ->
        val value = when (index) {
            0 -> item.team
            1 -> item.score1
            2 -> item.score2
            3 -> item.score3
            4 -> item.score4
            5 -> item.win.toString()
            6 -> item.draw.toString()
            7 -> item.defeat.toString()
            8 -> "${item.balls1}:${item.balls2}"
            9 -> item.points.toString()
            10 -> if (item.place > 0) item.place.toString() else ""
            else -> ""
        }

        if (value == EMPTY) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = ""
                )
            }
        } else {
            Text(
                text = value,
                color = when (index) {
                    5 -> ColorWin
                    6 -> ColorDraw
                    7 -> ColorDefeat
                    10 -> when (value) {
                        "1" -> ColorWin
                        "2" -> ColorDraw
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                    else -> MaterialTheme.colorScheme.onSurface
                },
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
        data = result,
        modifier = Modifier.padding(top = 4.dp),
        headerCellContent = headerCellTitle,
        cellContent = cellText,
        onClick = { onClick() }
    )
}
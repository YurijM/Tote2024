package com.mu.tote2024.presentation.screen.rating.chart

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.ui.common.UiState
import java.text.DecimalFormat
import kotlin.math.ceil

@SuppressLint("MutableCollectionMutableState")
@Composable
fun RatingChartScreen(
    viewModel: RatingChartViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
    var gameIds by remember { mutableStateOf<MutableList<Int>>(mutableListOf()) }
    var places by remember { mutableStateOf<ArrayList<Point>>(arrayListOf()) }
    var nickname by remember { mutableStateOf("") }

    val state by viewModel.state.collectAsState()
    val result = state.result

    LaunchedEffect(key1 = result) {
        when (result) {
            is UiState.Loading -> {
                isLoading = true
            }

            is UiState.Success -> {
                isLoading = false
                gameIds = viewModel.gameIds
                nickname = viewModel.nickname
                places = viewModel.places
                /*result.data
                    .filter { it.gamblerId == viewModel.gamblerId && it.gameId.toInt() in gameIds }
                    .sortedBy { it.gameId.toInt() }.forEachIndexed { index, stake ->
                        places.add(Point(index.toFloat(), stake.place.toFloat()))
                    }*/

            }

            is UiState.Error -> {
                isLoading = false
            }

            else -> {}
        }
    }

    if (places.size > 0) {
        val xAxisData = AxisData.Builder()
            .axisStepSize(25.dp)
            .backgroundColor(Color.Transparent)
            .steps(places.size - 1)
            .labelData { i -> (i + 1).toString() }
            .labelAndAxisLinePadding(8.dp)
            .build()

        val yAxisData = AxisData.Builder()
            .steps(viewModel.max.toInt() - viewModel.min.toInt())
            .backgroundColor(Color.Transparent)
            .labelAndAxisLinePadding(20.dp)
            .labelData { i ->
                val yScale = (viewModel.max - viewModel.min) / viewModel.max
                DecimalFormat("0").format(ceil(i * yScale) + 1)
            }.build()

        val lineChartData = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = places,
                        LineStyle(),
                        IntersectionPoint(),
                        SelectionHighlightPoint(),
                        ShadowUnderLine(),
                        SelectionHighlightPopUp()
                    )
                ),
            ),
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            gridLines = GridLines(),
            backgroundColor = Color.Transparent
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = nickname
            )
            /*Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = places.size.toString()
            )
            places.forEach {
                Text(it.x.toString())
            }*/
            /*LazyColumn {
            items(places) {
                Text(
                    text = it.x.toString(),
                    color = Color.Black
                )
            }
        }*/
            LineChart(
                modifier = Modifier
                    .fillMaxWidth(),
                //.height(50.dp),
                lineChartData = lineChartData
            )
        }
    }


    if (isLoading) {
        AppProgressBar()
    }
}
package com.mu.tote2024.presentation.screen.admin.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.mu.tote2024.R
import com.mu.tote2024.data.utils.Constants.GAMBLER
import com.mu.tote2024.presentation.components.AppTextField
import com.mu.tote2024.presentation.components.TextError
import com.mu.tote2024.presentation.ui.common.UiState

@Composable
fun AdminProfileScreen(
    viewModel: AdminProfileViewModel = hiltViewModel(),
    toBackstack: () -> Unit
) {
    val isLoading = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf("") }

    val state by viewModel.state.collectAsState()

    when (val result = state.result) {
        is UiState.Loading -> {
            isLoading.value = true
            error.value = ""
        }

        is UiState.Success -> {
            isLoading.value = false
            error.value = ""

            toBackstack()
        }

        is UiState.Error -> {
            isLoading.value = false

            error.value = result.message
        }

        else -> {}
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.profile_admin),
                style = MaterialTheme.typography.titleLarge
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 8.dp,
                            start = 8.dp,
                            end = 8.dp
                        )
                ) {
                    Row {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(.4f)
                                .fillMaxHeight()
                        ) {
                            SubcomposeAsyncImage(
                                model = GAMBLER.profile.photoUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .requiredSize(dimensionResource(id = R.dimen.profile_photo_size))
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop,
                                loading = {
                                    Box(
                                        modifier = Modifier.size(48.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            strokeWidth = 2.dp
                                        )
                                    }
                                }
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 4.dp)
                        ) {
                            Text(
                                text = GAMBLER.profile.nickname,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = GAMBLER.email,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = "Пол: ${GAMBLER.profile.gender}",
                                modifier = Modifier.padding(start = 8.dp),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                            Divider(
                                thickness = 1.dp,
                                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp),
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Spacer(modifier = Modifier.size(4.dp))
                            AppTextField(
                                label = stringResource(id = R.string.transferred_money),
                                value = viewModel.gambler.rate.toString(),
                                onChange = { newValue ->
                                    viewModel.onEvent(AdminProfileEvent.OnRateChange(newValue))
                                },
                                errorMessage = viewModel.errorRate
                            )
                        }
                    }

                }
                Divider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 8.dp,
                            end = 8.dp,
                            bottom = 8.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(
                            onClick = {
                                viewModel.onEvent(
                                    AdminProfileEvent.OnSave
                                )
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.save),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Button(
                            onClick = {
                               viewModel.onEvent(AdminProfileEvent.OnCancel)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.cancel),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    if (error.value.isNotBlank()) {
                        TextError(
                            errorMessage = error.value,
                            textAlign = TextAlign.Center
                        )
                    }
                }
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
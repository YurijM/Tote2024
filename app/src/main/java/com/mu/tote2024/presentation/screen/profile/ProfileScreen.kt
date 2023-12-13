package com.mu.tote2024.presentation.screen.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.data.utils.Constants.GAMBLER
import com.mu.tote2024.presentation.components.AppRadioGroup
import com.mu.tote2024.presentation.components.AppTextField
import com.mu.tote2024.presentation.ui.common.UiState

/*@Preview(
    name = "Light",
    showBackground = true
)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewProfileScreen() {
    Tote2024Theme {
        ProfileScreen(
            toMain = { }
        )
    }
}*/

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    toMain: () -> Unit
) {
    /*val sex = listOf("мужской", "женский")
    val setSelected = remember { mutableStateOf("") }*/

    //val gender = remember { mutableStateOf("") }

    val state by viewModel.state.collectAsState()

    when (state.result) {
        is UiState.Success -> {
            toMain()
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
                text = stringResource(id = R.string.profile),
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
                    Column(
                        modifier = Modifier
                            .width(132.dp)
                            .padding(end = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        /*Image(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "gambler",
                            modifier = Modifier.size(124.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                        )*/
                        Image(
                            painter = painterResource(id = R.drawable.mu),
                            contentDescription = null,
                            modifier = Modifier
                                .size(124.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier.height(24.dp),
                            contentPadding = PaddingValues(
                                top = 2.dp,
                                start = 8.dp,
                                end = 8.dp,
                                bottom = 4.dp
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.load_photo),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 4.dp)
                    ) {
                        Text(
                            text = GAMBLER.email,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            text = "Ставка 378 руб.",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Divider(
                            thickness = 2.dp,
                            modifier = Modifier.padding(top = 4.dp, bottom = 2.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "Пол",
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline
                            ),
                        ) {
                            AppRadioGroup(
                                items = viewModel.sex,
                                currentValue = viewModel.profile.gender,
                                onClick = { newValue ->
                                    viewModel.onEvent(ProfileEvent.OnGenderChange(newValue))
                                }
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 8.dp,
                            end = 8.dp,
                            bottom = 8.dp
                        )
                ) {
                    AppTextField(
                        label = stringResource(id = R.string.enter_nick),
                        value = viewModel.profile.nickname,
                        onChange = { newValue ->
                            viewModel.onEvent(ProfileEvent.OnNicknameChange(newValue))
                        },
                        errorMessage = viewModel.profileErrors.errorNickname
                    )
                }
            }
        }
    }
}
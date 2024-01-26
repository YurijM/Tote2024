package com.mu.tote2024.presentation.screen.admin.email.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.domain.model.EmailModel
import com.mu.tote2024.presentation.components.AppFabAdd
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.Title
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.ID_NEW_EMAIL

@Composable
fun AdminEmailListScreen(
    viewModel: AdminEmailListViewModel = hiltViewModel(),
    toEmail: (String) -> Unit
) {
    val isLoading = remember { mutableStateOf(false) }
    var emailList by remember { mutableStateOf<List<EmailModel>>(listOf()) }

    val state by viewModel.state.collectAsState()

    when (val result = state.result) {
        is UiState.Loading -> {
            isLoading.value = true
        }

        is UiState.Success -> {
            isLoading.value = false

            emailList = result.data
                .sortedBy { it.email }
        }

        is UiState.Error -> {
            isLoading.value = false
        }

        else -> {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Title(R.string.admin_email_list)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(.75f)
                .fillMaxHeight()
        ) {
            items(emailList) { email ->
                AdminEmailItemScreen(
                    email = email,
                    onDelete = {
                        viewModel.onEvent((AdminEmailListEvent.OnDelete(email.emailId)))
                    }
                )
            }
        }
    }
    AppFabAdd(
        onAdd = { toEmail(ID_NEW_EMAIL) }
    )

    if (isLoading.value) {
        AppProgressBar()
    }
}


package com.mu.tote2024.presentation.screen.admin.gambler.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.mu.tote2024.R
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.presentation.utils.Constants.NO

@Composable
fun AdminGamblerItemScreen(
    gambler: GamblerModel,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 4.dp,
                start = 8.dp,
                end = 8.dp
            )
            .clickable { gambler.gamblerId?.let { onClick(it) } },
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubcomposeAsyncImage(
            model = gambler.profile.photoUrl,
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier.size(dimensionResource(id = R.dimen.app_bar_no_name_size)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.fillMaxSize(.5f),
                        strokeWidth = 1.dp
                    )
                }
            },
        )
        Text(
            text = gambler.profile.nickname + if (gambler.admin) " (администратор)" else "",
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = if (gambler.rate > 0) "${gambler.rate} руб." else NO,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
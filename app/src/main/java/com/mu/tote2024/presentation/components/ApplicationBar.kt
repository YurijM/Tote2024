package com.mu.tote2024.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.mu.tote2024.R

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
fun PreviewApplicationBar() {
    Tote2024Theme {
        ApplicationBar(
            photoUrl = "",
            isAdmin = true,
            onImageClick = { }
        )
    }
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationBar(
    photoUrl: String,
    isAdmin: Boolean,
    onImageClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface
            ),
            title = {
                Text(text = stringResource(id = R.string.app_name))
            },
            actions = {
                if (photoUrl.isNotBlank()) {
                    SubcomposeAsyncImage(
                        model = photoUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.app_bar_photo_size))
                            .clip(CircleShape)
                            .clickable { onImageClick() },
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
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.noname),
                        contentDescription = "gambler",
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.app_bar_no_name_size))
                            .clip(CircleShape)
                            .clickable { onImageClick() },
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }
                if (isAdmin) {
                    IconButton(
                        onClick = { TODO() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "settings"
                        )
                    }
                }
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "exit"
                    )
                }
            }
        )
    }
}
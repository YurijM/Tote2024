package com.mu.tote2024.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.mu.tote2024.R
import com.mu.tote2024.presentation.utils.Constants.Routes.ADMIN_MAIN_SCREEN

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
    navController: NavHostController,
    photoUrl: String,
    isAdmin: Boolean,
    onImageClick: () -> Unit,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface
            ),
            title = {
                Text(text = stringResource(id = R.string.app_name))
            },
            actions = {
                if (photoUrl.isNotBlank()) {
                    /*SubcomposeAsyncImage(
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
                    )*/
                    val placeholder = rememberVectorPainter(
                        image = Icons.Rounded.AccountCircle
                    )
                    AsyncImage(
                        model = photoUrl,
                        placeholder = placeholder,
                        contentDescription = "User Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.app_bar_photo_size))
                            //.size(36.dp)
                            .aspectRatio(1f / 1f)
                            .clip(CircleShape)
                            .clickable {
                                onImageClick()
                            }
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
                        onClick = { navController.navigate(ADMIN_MAIN_SCREEN) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "settings"
                        )
                    }
                }
                IconButton(
                    onClick = {
                        onSignOut()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "exit"
                    )
                }
            }
        )
    }
}
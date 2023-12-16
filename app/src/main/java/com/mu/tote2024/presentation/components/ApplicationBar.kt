package com.mu.tote2024.presentation.components

import android.content.res.Configuration
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.mu.tote2024.R
import com.mu.tote2024.data.utils.Constants.GAMBLER
import com.mu.tote2024.presentation.ui.Tote2024Theme

@Preview(
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
            isAdmin = true,
            onImageClick = { }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationBar(
    isAdmin: Boolean,
    onImageClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.smallTopAppBarColors(
                actionIconContentColor = MaterialTheme.colorScheme.onSurface
            ),
            title = {
                Text(text = stringResource(id = R.string.app_name))
            },
            actions = {
                SubcomposeAsyncImage(
                    model = GAMBLER.profile?.photoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .clickable { onImageClick() }
                ) {
                    val state = painter.state
                    if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.fillMaxSize(.25f)
                            )
                        }
                    } else {
                        SubcomposeAsyncImageContent()
                    }
                }
               /* Image(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .clickable { onImageClick() },
                    painter = painterResource(id = R.drawable.mu),
                    contentDescription = "user_photo"
                )*/
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
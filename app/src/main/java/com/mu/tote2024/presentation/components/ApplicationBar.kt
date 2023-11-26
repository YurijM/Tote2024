package com.mu.tote2024.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mu.tote2024.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationBar(
    isAdmin: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider(color = Color.White)
        TopAppBar(
            /*colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = ColorApplication,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White
            ),*/
            title = {
                Text(text = stringResource(id = R.string.app_name))
            },
            actions = {
                Image(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .clickable {  },
                    painter = painterResource(id = R.drawable.mu),
                    contentDescription = "user_photo"
                )
                if (isAdmin) {
                    IconButton(
                        onClick = { /*TODO*/ }
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
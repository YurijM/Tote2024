package com.mu.tote2024.presentation.screen.rating

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mu.tote2024.R
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.presentation.ui.ColorDraw
import com.mu.tote2024.presentation.ui.ColorWin

@Composable
fun RatingWinItemScreen(
    gambler: GamblerModel,
    winnings: Int
) {
    val color = when (gambler.result.place) {
        1 -> ColorWin
        2 -> ColorDraw
        else -> MaterialTheme.colorScheme.onSurface
    }
    Card(
        modifier = Modifier
            //.fillMaxWidth()
            .width(dimensionResource(id = R.dimen.winner_card_size))
            .padding(4.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        ),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.place, gambler.result.place.toString()),
                color = color
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                /*SubcomposeAsyncImage(
                    model = gambler.profile.photoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .requiredSize(dimensionResource(id = R.dimen.winner_photo_size))
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
                )*/
                val placeholder = rememberVectorPainter(
                    image = Icons.Rounded.AccountCircle
                )
                AsyncImage(
                    model = gambler.profile.photoUrl,
                    placeholder = placeholder,
                    contentDescription = "User Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .requiredSize(dimensionResource(id = R.dimen.winner_photo_size))
                        .clip(RoundedCornerShape(8.dp)),
                        //.size(64.dp)
                        //.aspectRatio(1f / 1f)
                        //.clip(CircleShape)
                )
            }
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = gambler.profile.nickname,
                color = color
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = winnings.toString(),
                    color = color
                )
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(id = R.drawable.ic_ruble),
                    tint = color,
                    contentDescription = ""
                )
            }
        }
    }
}
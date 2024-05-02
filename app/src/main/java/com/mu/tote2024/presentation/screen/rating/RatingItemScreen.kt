package com.mu.tote2024.presentation.screen.rating

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import com.mu.tote2024.presentation.ui.ColorDefeat
import com.mu.tote2024.presentation.ui.ColorDraw
import com.mu.tote2024.presentation.ui.ColorFine
import com.mu.tote2024.presentation.ui.ColorWin
import java.text.DecimalFormat

@Composable
fun RatingItemScreen(
    nickname: String,
    photoUrl: String,
    points: Double,
    place: Int,
    prevPlace: Int,
    showArrow: Boolean,
    toAdminGamblerPhoto: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 4.dp,
                horizontal = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubcomposeAsyncImage(
            model = photoUrl,
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .clickable {
                    toAdminGamblerPhoto()
                },
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
            text = nickname,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = DecimalFormat("0.00").format(points),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = place.toString(),
            color = when (place) {
                1 -> ColorWin
                2 -> ColorDraw
                3 -> MaterialTheme.colorScheme.onSurface
                else -> ColorDefeat
            }
        )
        if (showArrow) {
            when {
                (place < prevPlace) -> Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    tint = ColorWin,
                    contentDescription = ""
                )

                (place > prevPlace) -> Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    tint = ColorFine,
                    contentDescription = ""
                )

                else -> Spacer(modifier = Modifier.size(20.dp))
            }
        }
       /* Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = place.toString(),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = prevPlace.toString(),
            color = MaterialTheme.colorScheme.onSurface
        )*/
    }
}
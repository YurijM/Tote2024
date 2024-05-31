package com.mu.tote2024.presentation.screen.rating

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.mu.tote2024.R
import com.mu.tote2024.presentation.ui.ColorDefeat
import com.mu.tote2024.presentation.ui.ColorDraw
import com.mu.tote2024.presentation.ui.ColorWin
import java.text.DecimalFormat

@Composable
fun RatingItemScreen(
    nickname: String,
    photoUrl: String,
    points: Double,
    prevPoints: Double,
    place: Int,
    prevPlace: Int,
    showArrows: Boolean,
    toAdminGamblerPhoto: () -> Unit
) {
    val step = prevPlace - place
    var icon: Painter = painterResource(id = R.drawable.ic_up)
    var color: Color = ColorWin
    var height = 16.dp
    var fontSize = MaterialTheme.typography.labelSmall.fontSize

    if (prevPlace < place) {
        icon = painterResource(id = R.drawable.ic_down)
        color = ColorDraw
    } else if (place == prevPlace) {
        height = 0.dp
        fontSize = 0.sp
    }

    val colorPlace: Color = when (place) {
        1 -> ColorWin
        2 -> ColorDraw
        3 -> MaterialTheme.colorScheme.onSurface
        else -> ColorDefeat
    }

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

        if (showArrows) {
            Text(
                modifier = Modifier.width(68.dp),
                text = "(${DecimalFormat("0.00").format(prevPoints)})",
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center,
                text = place.toString(),
                color = colorPlace
            )
            Row(
                modifier = Modifier.width(32.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.height(height),
                    painter = icon,
                    tint = color,
                    contentDescription = ""
                )
                Text(
                    text = (if (step > 0) "+" else "") + step.toString(),
                    color = color,
                    fontSize = fontSize
                )
            }
        }
    }
}

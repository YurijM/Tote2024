package com.mu.tote2024.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mu.tote2024.R
import com.mu.tote2024.presentation.utils.Constants.YEAR_START

/*@Preview(
    name = "Light",
    showBackground = true
)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun PreviewBottomNav() {
    Tote2024Theme {
        BottomNav()
    }
}*/

@Composable
fun BottomNav(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val navItems = listOf(
        BottomNavItem.RatingItem,
        BottomNavItem.StakeItem,
        BottomNavItem.PrognosisItem,
        BottomNavItem.GamesItem,
    )
    Column {
        BottomAppBar {
            navItems.forEach { item ->
                val title = stringResource(id = item.titleId)

                NavigationBarItem(
                    selected = (item.route == currentRoute),
                    onClick = { onNavigate(item.route) },
                    icon = {
                        Icon(
                            painter = painterResource(id = item.iconId),
                            contentDescription = title
                        )
                    },
                    label = {
                        Text(
                            text = title
                        )
                    },
                    alwaysShowLabel = true
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.author),
                contentDescription = "author",
                modifier =Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$YEAR_START-2024 ©",
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
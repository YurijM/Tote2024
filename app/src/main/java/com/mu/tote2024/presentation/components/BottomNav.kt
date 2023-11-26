package com.mu.tote2024.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mu.tote2024.R

@Composable
fun BottomNav() {
    val navItems = listOf(
        BottomNavItem.RatingItem,
        BottomNavItem.StakeItem,
        BottomNavItem.PrognosisItem,
        BottomNavItem.GamesItem,
    )
    Column {
        BottomAppBar(
            //containerColor = Color.White
        ) {
            navItems.forEachIndexed { index, item ->
                val title = stringResource(id = item.titleId)

                NavigationBarItem(
                    /*colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ColorApplication,
                        selectedTextColor = ColorApplication,
                        unselectedIconColor = Color.LightGray,
                        unselectedTextColor = Color.LightGray,
                        indicatorColor = ColorBackground
                    ),*/
                    selected = (index == 3),
                    onClick = { /*TODO*/ },
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
                    alwaysShowLabel = false
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                //.background(ColorApplication)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier =Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.author_gray),
                contentDescription = "author"
            )
            Text(
                text = "2023-2024 ©",
                //color = Color.White
            )
        }
        Divider(color = Color.White)
    }
}
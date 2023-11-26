package com.mu.tote2024.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mu.tote2024.R
import com.mu.tote2024.presentation.ui.Color3
import com.mu.tote2024.presentation.ui.Tote2024Theme

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
fun PreviewLogonButton() {
    Tote2024Theme {
        LogonButton(
            titleId = R.string.sign_up,
            onClick = {}
        )
    }
}*/

@Composable
fun LogonButton(
   @StringRes titleId: Int,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = { onClick() }
    ) {
        Text(
            text = stringResource(titleId),
            style = TextStyle(
                color = Color3,
                fontSize = 20.sp,
                shadow = Shadow(
                    offset = Offset(4f, 5f),
                    blurRadius = 5f
                )
            )
        )
    }
}
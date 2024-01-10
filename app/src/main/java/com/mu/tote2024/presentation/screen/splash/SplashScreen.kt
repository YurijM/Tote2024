package com.mu.tote2024.presentation.screen.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(
    toAuth: () -> Unit,
    toMain: () -> Unit
) {
    val viewModel: SplashViewModel = hiltViewModel()

    val scale = remember {
        Animatable(0f)
    }
    val scaleText = remember {
        Animatable(0f)
    }

    val developmentText = stringResource(R.string.design) + "\n"
    val companyText = stringResource(R.string.company)
    val text = buildAnnotatedString {
        append("$developmentText ")
        withStyle(
            style = SpanStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Black
            )
        ) {
            pushStringAnnotation(tag = companyText, annotation = companyText)
            append(companyText)
        }
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 3000,
                easing = {
                    OvershootInterpolator(10f).getInterpolation(it)
                }
            )
        )
        scaleText.animateTo(
            targetValue = .85f,
            animationSpec = tween(
                durationMillis = 3000,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                }
            )
        )
        delay(1000L)
        if (viewModel.isAuth) toMain() else toAuth()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.scale(scale.value),
                painter = painterResource(id = R.drawable.author),
                contentDescription = "Logo"
            )
            AssistChip(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .scale(scaleText.value),
                onClick = { /*TODO*/ },
                label = {
                    Text(
                        text = text,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "settings"
                    )
                }
            )
        }
    }
}

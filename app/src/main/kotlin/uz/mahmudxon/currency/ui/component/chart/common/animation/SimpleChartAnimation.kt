package uz.mahmudxon.currency.ui.component.chart.common.animation

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.TweenSpec

fun simpleChartAnimation() =
    TweenSpec<Float>(durationMillis = 3000, delay = 500, easing = FastOutLinearInEasing)
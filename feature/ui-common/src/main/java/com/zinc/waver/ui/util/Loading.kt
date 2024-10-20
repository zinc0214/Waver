package com.zinc.waver.ui.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.zinc.waver.ui_common.R

@Composable
fun Loading(modifier: Modifier = Modifier.fillMaxSize()) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.wave_loading))
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier.sizeIn(maxWidth = 100.dp, maxHeight = 100.dp),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition,
                modifier = modifier.size(100.dp),
                iterations = LottieConstants.IterateForever
            )
        }
    }
}

@Preview
@Composable
private fun LoadingPreview() {
    Loading()
}
package com.zinc.waver.ui.presentation.screen.ads

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdFullScreen(modifier: Modifier = Modifier) {
    val writeId = "ca-app-pub-3940256099942544/6300978111"
    val adRequest = AdRequest.Builder().build()

    Box(modifier = modifier) {
        AndroidView(modifier = Modifier.fillMaxSize(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.FULL_BANNER)
                    adUnitId = writeId
                    loadAd(adRequest)
                }
            },
            update = { adView ->
                adView.loadAd(adRequest)
            })
    }

}


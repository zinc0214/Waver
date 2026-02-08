package com.zinc.waver.ui.presentation.screen.ads

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BadgeAdBannerScreen(modifier: Modifier = Modifier) {
    val adUnitId =
        "ca-app-pub-6302671173915322/6244920706" //"ca-app-pub-3940256099942544/6300978111"
    val adRequest = AdRequest.Builder().build()

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    this.adUnitId = adUnitId
                    loadAd(adRequest)
                }
            },
            update = { adView ->
                adView.loadAd(adRequest)
            })
    }
}

@Composable
fun MoreAdBannerScreen(modifier: Modifier = Modifier) {
    val adUnitId =
        "ca-app-pub-6302671173915322/5768959004"
    val adRequest = AdRequest.Builder().build()

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    this.adUnitId = adUnitId
                    loadAd(adRequest)
                }
            },
            update = { adView ->
                adView.loadAd(adRequest)
            })
    }
}

package com.zinc.waver.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp


@Composable
fun LoadNativeAd() {
    val context = LocalContext.current

    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    if (nativeAd != null) {
        CallNativeAd(nativeAd!!)
    }

    LaunchedEffect(Unit) {
        loadNativeAd(context, "ca-app-pub-3940256099942544/2247696110") {
            nativeAd = it
            Log.e("ayhan", "nativeAd : $nativeAd")
        }
    }

    nativeAd?.let { CallNativeAd(it) }
}

@Composable
fun CallNativeAd(nativeAd: NativeAd) {
    NativeAdView(ad = nativeAd) { ad, view ->
        LoadAdContent(ad, view)
    }
}

@Composable
fun NativeAdView(
    ad: NativeAd,
    adContent: @Composable (ad: NativeAd, contentView: View) -> Unit,
) {
    val contentViewId by remember { mutableIntStateOf(View.generateViewId()) }
    val adViewId by remember { mutableIntStateOf(View.generateViewId()) }

    AndroidView(
        factory = { context ->
            val contentView = ComposeView(context).apply {
                id = contentViewId
            }
            NativeAdView(context).apply {
                id = adViewId
                addView(contentView)
            }
        },
        update = { view ->
            val adView = view.findViewById<NativeAdView>(adViewId)
            val contentView = view.findViewById<ComposeView>(contentViewId)

            adView.setNativeAd(ad)
            adView.callToActionView = contentView
            contentView.setContent { adContent(ad, contentView) }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LoadAdContent(nativeAd: NativeAd?, composeView: View) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .combinedClickable {
                composeView.performClick()
            },
    ) {
        nativeAd?.let {
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.Start
                ) {
                    val icon: Drawable? = it.icon?.drawable
                    icon?.let { drawable ->
                        Image(
                            painter = rememberAsyncImagePainter(model = drawable),
                            contentDescription = "Ad"/*it.icon?.contentDescription*/,
                            modifier = Modifier.size(50.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column {
                        Text(
                            text = it.headline ?: "",
                            color = Gray10,
                            fontSize = dpToSp(16.dp),
                        )
                        Text(
                            text = it.body ?: "",
                            color = Gray10,
                            fontSize = dpToSp(16.dp),
                        )
                    }
                }

                it.callToAction?.let { cta ->
                    Button(
                        modifier = Modifier,
                        onClick = {
                            composeView.performClick()
                        },
                        content = {
                            MyText(
                                text = cta.uppercase(),
                                color = Gray10,
                                fontSize = dpToSp(16.dp),
                            )
                        }
                    )
                }
            }
        } ?: run {
            // Placeholder for loading state or error state
            Text("Loading ad...")
        }
    }
}

@Composable
        /** Display a native ad with a user defined template. */
fun DisplayNativeAdView(nativeAd: NativeAd) {
    val context = LocalContext.current

    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    LaunchedEffect(null) {
        loadNativeAd(context, "ca-app-pub-6302671173915322/2390711536") {
            nativeAd = it
        }
    }
}


fun loadNativeAd(context: Context, adUnitId: String, callback: (NativeAd?) -> Unit) {
    Log.e("ayhan", "loadNativeAd")
    val builder = AdLoader.Builder(context, adUnitId)
        .forNativeAd { nativeAd ->
            Log.e("ayhan", "loadNativeAd nativeAd :$nativeAd")
            callback(nativeAd)
        }

    val adLoader = builder
        .withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("ayhan", "AdError : $adError")
                callback(null)
            }
        })
        .withNativeAdOptions(NativeAdOptions.Builder().build())
        .build()

    adLoader.loadAd(AdRequest.Builder().build())
}


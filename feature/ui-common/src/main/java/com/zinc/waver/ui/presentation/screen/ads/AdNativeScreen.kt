package com.zinc.waver.ui.presentation.screen.ads

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.AdChoicesView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray5
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.util.shadow

// [START ad_listener]
@Composable
        /** Load and display a native ad. */
fun NativeScreen() {
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    val context = LocalContext.current
    var isDisposed by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        // Load the native ad when we launch this screen
        loadNativeAd(
            context = context,
            onAdLoaded = { ad ->
                // Handle the native ad being loaded.
                if (!isDisposed) {
                    nativeAd = ad
                } else {
                    // Destroy the native ad if loaded after the screen is disposed.
                    ad.destroy()
                }
            },
        )
        // Destroy the native ad to prevent memory leaks when we dispose of this screen.
        onDispose {
            isDisposed = true
            nativeAd?.destroy()
            nativeAd = null
        }
    }

    // Display the native ad view with a user defined template.
    nativeAd?.let { adValue -> DisplayNativeAdView(adValue) }
}

fun loadNativeAd(context: Context, onAdLoaded: (NativeAd) -> Unit) {
    val adLoader =
        AdLoader.Builder(
            context,
            "ca-app-pub-3940256099942544/2247696110"
        ) // "ca-app-pub-6302671173915322/2390711536"
            .forNativeAd { nativeAd -> onAdLoaded(nativeAd) }
            .withAdListener(
                object : AdListener() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.e("ayhan", "Native ad failed to load: ${error.message}")
                    }

                    override fun onAdLoaded() {
                        Log.e("ayhan", "Native ad was loaded.")
                    }

                    override fun onAdImpression() {
                        Log.e("ayhan", "Native ad recorded an impression.")
                    }

                    override fun onAdClicked() {
                        Log.e("ayhan", "Native ad was clicked.")
                    }
                }
            )
            .build()
    adLoader.loadAd(AdRequest.Builder().build())
}

// [END ad_listener]

// [START display_native_ad]
@Composable
fun DisplayNativeAdView(nativeAd: NativeAd) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Gray1),
        modifier = Modifier
            .background(color = Gray1, shape = RoundedCornerShape(8.dp))
            .shadow(
                color = Gray5.copy(alpha = 0.2f),
                offsetX = (0).dp,
                offsetY = (0).dp,
                blurRadius = 8.dp,
            )
            .clip(
                RoundedCornerShape(8.dp)
            )
    ) {
        // Call the NativeAdView composable to display the native ad.
        NativeAdView(nativeAd) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, start = 17.dp, end = 17.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // If available, display the icon asset.
                        nativeAd.icon?.let { icon ->
                            NativeAdIconView(
                                Modifier
                                    .size(32.dp)
                            ) {
                                icon.drawable?.toBitmap()?.let { bitmap ->
                                    Image(bitmap = bitmap.asImageBitmap(), "Icon")
                                }
                            }
                        }
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            // If available, display the headline asset.
                            nativeAd.headline?.let {
                                NativeAdHeadlineView {
                                    MyText(
                                        text = it,
                                        fontSize = dpToSp(12.dp)
                                    )
                                }
                            }
                            // If available, display the star rating asset.
                            nativeAd.starRating?.let {
                                NativeAdStarRatingView {
                                    MyText(
                                        text = "Rated $it",
                                        fontSize = dpToSp(12.dp)
                                    )
                                }
                            }
                        }
                    }
                    // Display the ad attribution.
                    NativeAdAttribution(
                        modifier = Modifier.align(Alignment.TopEnd),
                        text = "Ad",
                    )
                }

                // If available, display the body asset.
                nativeAd.body?.let {
                    NativeAdBodyView(modifier = Modifier.padding(horizontal = 20.dp)) {
                        MyText(
                            text = it,
                            fontSize = dpToSp(18.dp)
                        )
                    }
                }

                // Display the media asset.
                NativeAdMediaView(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    scaleType = ImageView.ScaleType.FIT_CENTER
                )

                nativeAd.callToAction?.let { callToAction ->
                    NativeAdCallToActionView(
                        Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                    ) {
                        NativeAdButton(text = callToAction)
                    }
                }


                Spacer(modifier = Modifier.height(20.dp))
//                Row(
//                    Modifier
//                        .align(Alignment.End)
//                        .padding(5.dp)
//                ) {
//                    // If available, display the price asset.
//                    nativeAd.price?.let {
//                        NativeAdPriceView(
//                            Modifier
//                                .padding(5.dp)
//                                .align(Alignment.CenterVertically)
//                        ) {
//                            MyText(text = it)
//                        }
//                    }
//                    // If available, display the store asset.
//                    nativeAd.store?.let {
//                        NativeAdStoreView(
//                            Modifier
//                                .padding(5.dp)
//                                .align(Alignment.CenterVertically)
//                        ) {
//                            MyText(text = it)
//                        }
//                    }
//                    // If available, display the call to action asset.
//                    nativeAd.callToAction?.let { callToAction ->
//                        NativeAdCallToActionView(Modifier.padding(5.dp)) { NativeAdButton(text = callToAction) }
//                    }
//                }
            }
        }
    }
}

// [END display_native_ad]

@Preview
@Composable
fun NativeLayoutScreenPreview() {
    NativeScreen()
}

/**
 * A CompositionLocal that can provide a `NativeAdView` to ad attributes such as `NativeHeadline`.
 */
internal val LocalNativeAdView = staticCompositionLocalOf<NativeAdView?> { null }

/**
 * This is the Compose wrapper for a NativeAdView.
 *
 * @param nativeAd The `NativeAd` object containing the ad assets to be displayed in this view.
 * @param modifier The modifier to apply to the native ad.
 * @param content A composable function that defines the rest of the native ad view's elements.
 */
@Composable
fun NativeAdView(
    nativeAd: NativeAd,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val nativeAdViewRef = remember { mutableStateOf<NativeAdView?>(null) }
    AndroidView(
        factory = { context ->
            val composeView =
                ComposeView(context).apply {
                    layoutParams =
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                }
            NativeAdView(context).apply {
                layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                addView(composeView)
                nativeAdViewRef.value = this
            }
        },
        modifier = modifier,
        update = { view ->
            val composeView = view.getChildAt(0) as? ComposeView
            composeView?.setContent {
                // Set `nativeAdView` as the current LocalNativeAdView so that
                // `content` can access the `NativeAdView` via `LocalNativeAdView.current`.
                // This would allow ad attributes (such as `NativeHeadline`) to attribute
                // its contained View subclass via setter functions (e.g. nativeAdView.headlineView =
                // view)
                CompositionLocalProvider(LocalNativeAdView provides view) { content() }
            }
        },
    )
    val currentNativeAd by rememberUpdatedState(nativeAd)
    SideEffect { nativeAdViewRef.value?.setNativeAd(currentNativeAd) }
}

/**
 * The ComposeWrapper container for an advertiserView inside a NativeAdView. This composable must be
 * invoked from within a `NativeAdView`.
 *
 * @param modifier modify the native ad view element.
 * @param content A composable function that defines the content of this native asset.
 */
@Composable
fun NativeAdAdvertiserView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val nativeAdView = LocalNativeAdView.current ?: throw IllegalStateException("NativeAdView null")
    AndroidView(
        factory = { context -> ComposeView(context) },
        modifier = modifier,
        update = { view ->
            nativeAdView.advertiserView = view
            view.setContent(content)
        },
    )
}

/**
 * The ComposeWrapper container for a bodyView inside a NativeAdView. This composable must be
 * invoked from within a `NativeAdView`.
 *
 * @param modifier modify the native ad view element.
 * @param content A composable function that defines the content of this native asset.
 */
@Composable
fun NativeAdBodyView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val nativeAdView = LocalNativeAdView.current ?: throw IllegalStateException("NativeAdView null")
    AndroidView(
        factory = { context -> ComposeView(context) },
        modifier = modifier,
        update = { view ->
            nativeAdView.bodyView = view
            view.setContent(content)
        },
    )
}

/**
 * The ComposeWrapper container for a callToActionView inside a NativeAdView. This composable must
 * be invoked from within a `NativeAdView`.
 *
 * @param modifier modify the native ad view element.
 * @param content A composable function that defines the content of this native asset.
 */
@Composable
fun NativeAdCallToActionView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val nativeAdView = LocalNativeAdView.current ?: throw IllegalStateException("NativeAdView null")
    AndroidView(
        factory = { context -> ComposeView(context) },
        modifier = modifier,
        update = { view ->
            nativeAdView.callToActionView = view
            view.setContent(content)
        },
    )
}

/**
 * The ComposeWrapper for a adChoicesView inside a NativeAdView. This composable must be invoked
 * from within a `NativeAdView`.
 *
 * @param modifier modify the native ad view element.
 */
@Composable
fun NativeAdChoicesView(modifier: Modifier = Modifier) {
    val nativeAdView = LocalNativeAdView.current ?: throw IllegalStateException("NativeAdView null")
    AndroidView(
        factory = { context ->
            AdChoicesView(context).apply {
                minimumWidth = 15
                minimumHeight = 15
            }
        },
        modifier = modifier,
        update = { view -> nativeAdView.adChoicesView = view },
    )
}

/**
 * The ComposeWrapper container for a headlineView inside a NativeAdView. This composable must be
 * invoked from within a `NativeAdView`.
 *
 * @param modifier modify the native ad view element.
 * @param content A composable function that defines the content of this native asset.
 */
@Composable
fun NativeAdHeadlineView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val nativeAdView = LocalNativeAdView.current ?: throw IllegalStateException("NativeAdView null")
    AndroidView(
        factory = { context -> ComposeView(context) },
        modifier = modifier,
        update = { view ->
            nativeAdView.headlineView = view
            view.setContent(content)
        },
    )
}

/**
 * The ComposeWrapper container for a iconView inside a NativeAdView. This composable must be
 * invoked from within a `NativeAdView`.
 *
 * @param modifier modify the native ad view element.
 * @param content A composable function that defines the content of this native asset.
 */
@Composable
fun NativeAdIconView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val nativeAdView = LocalNativeAdView.current ?: throw IllegalStateException("NativeAdView null")
    AndroidView(
        factory = { context -> ComposeView(context) },
        modifier = modifier,
        update = { view ->
            nativeAdView.iconView = view
            view.setContent(content)
        },
    )
}

/**
 * The ComposeWrapper for a mediaView inside a NativeAdView. This composable must be invoked from
 * within a `NativeAdView`.
 *
 * @param modifier modify the native ad view element.
 * @param scaleType The ImageView.ScaleType to apply to the image/media within the MediaView.
 */
@Composable
fun NativeAdMediaView(modifier: Modifier = Modifier, scaleType: ImageView.ScaleType? = null) {
    val nativeAdView = LocalNativeAdView.current ?: throw IllegalStateException("NativeAdView null")
    AndroidView(
        factory = { context -> MediaView(context) },
        update = { view ->
            nativeAdView.mediaView = view
            scaleType?.let { type -> view.setImageScaleType(type) }
        },
        modifier = modifier,
    )
}

/**
 * The ComposeWrapper container for a priceView inside a NativeAdView. This composable must be
 * invoked from within a `NativeAdView`.
 *
 * @param modifier modify the native ad view element.
 * @param content A composable function that defines the content of this native asset.
 */
@Composable
fun NativeAdPriceView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val nativeAdView = LocalNativeAdView.current ?: throw IllegalStateException("NativeAdView null")
    AndroidView(
        factory = { context -> ComposeView(context) },
        modifier = modifier,
        update = { view ->
            nativeAdView.priceView = view
            view.setContent(content)
        },
    )
}

/**
 * The ComposeWrapper container for a starRatingView inside a NativeAdView. This composable must be
 * invoked from within a `NativeAdView`.
 *
 * @param modifier modify the native ad view element.
 * @param content A composable function that defines the content of this native asset.
 */
@Composable
fun NativeAdStarRatingView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val nativeAdView = LocalNativeAdView.current ?: throw IllegalStateException("NativeAdView null")
    AndroidView(
        factory = { context -> ComposeView(context) },
        modifier = modifier,
        update = { view ->
            nativeAdView.starRatingView = view
            view.setContent(content)
        },
    )
}

/**
 * The ComposeWrapper container for a storeView inside a NativeAdView. This composable must be
 * invoked from within a `NativeAdView`.
 *
 * @param modifier modify the native ad view element.
 * @param content A composable function that defines the content of this native asset.
 */
@Composable
fun NativeAdStoreView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val nativeAdView = LocalNativeAdView.current ?: throw IllegalStateException("NativeAdView null")
    AndroidView(
        factory = { context -> ComposeView(context) },
        modifier = modifier,
        update = { view ->
            nativeAdView.storeView = view
            view.setContent(content)
        },
    )
}

/**
 * The composable for a ad attribution inside a NativeAdView. This composable must be invoked from
 * within a `NativeAdView`.
 *
 * @param modifier modify the native ad view element.
 * @param text The string identifying this view as an advertisement.
 * @param containerColor The background color of the attribution.
 * @param contentColor The text color of the attribution.
 * @param padding The padding around the attribution text.
 */
@Composable
fun NativeAdAttribution(
    modifier: Modifier = Modifier,
    text: String = "Ad",
    containerColor: Color = ButtonDefaults.buttonColors().containerColor,
    contentColor: Color = ButtonDefaults.buttonColors().contentColor,
    padding: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
) {
    Box(
        modifier = modifier
            .background(containerColor, RoundedCornerShape(2.dp))
            .padding(padding)
    ) {
        MyText(color = contentColor, text = text)
    }
}

/**
 * The composable for a button inside a NativeAdView. This composable must be invoked from within a
 * NativeAdView.
 *
 * The Jetpack Compose button implements a click handler which overrides the native ad click
 * handler, causing issues. The NativeAdButton does not implement a click handler. To handle native
 * ad clicks, use the NativeAd AdListener onAdClicked callback.
 *
 * @param text The string identifying this view as an advertisement.
 * @param modifier modify the native ad view element.
 * @param containerColor The background color of the button.
 * @param contentColor The text color of the button.
 * @param padding The padding around the button text.
 */
@Composable
fun NativeAdButton(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = ButtonDefaults.buttonColors().containerColor,
    contentColor: Color = ButtonDefaults.buttonColors().contentColor,
    padding: PaddingValues = ButtonDefaults.ContentPadding,
) {
    Box(
        modifier = modifier
            .background(containerColor, RoundedCornerShape(2.dp))
            .padding(padding)
    ) {
        MyText(color = contentColor, text = text, fontSize = dpToSp(15.dp))
    }
}

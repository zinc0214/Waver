package com.zinc.waver.ui.util

import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier, fontSize: Dp, @ColorInt textColor: Int) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
//                val typeface = ResourcesCompat.getFont(context, R.font.notosans_kr_regular)
//                Log.e("ayhan", "typeSpace : $typeface")
//                setTypeface(typeface)
                textSize = fontSize.value
                setTextColor(textColor)
            }
        },
        update = { it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    )
}
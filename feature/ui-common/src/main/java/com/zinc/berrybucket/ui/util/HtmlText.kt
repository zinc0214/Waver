package com.zinc.berrybucket.ui.util

import android.util.Log
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import com.zinc.berrybucket.ui_common.R


@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier, fontSize: Dp, @ColorInt textColor: Int) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                val typeface = ResourcesCompat.getFont(context, R.font.notosans_kr_regular)
                Log.e("ayhan", "typeSpace : $typeface")
                setTypeface(typeface)
                textSize = fontSize.value
                setTextColor(textColor)
            }
        },
        update = { it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    )
}
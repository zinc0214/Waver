package com.zinc.waver.ui.util

import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import com.zinc.waver.ui_common.R

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

@Composable
fun HtmlText2(
    html: String,
    modifier: Modifier = Modifier,
    fontSize: Dp,
    fontFamily: Int = R.font.pretendard_regular
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                val typeface = ResourcesCompat.getFont(
                    context,
                    fontFamily
                ) // fontFamily 인자를 사용
                typeface?.let { setTypeface(it) } // null 체크 후 적용

                textSize = fontSize.value
            }
        },
        update = { it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    )
}
package com.zinc.mybury_2.compose.ui.component

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import com.zinc.mybury_2.compose.theme.BaseTheme

class BucketCircularProgressBarWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AbstractComposeView(context, attrs, defStyle) {

    private lateinit var animFinishEvent: () -> Unit

    fun setAnimFinishEvent(event: () -> Unit) {
        animFinishEvent = event
    }

    @Composable
    override fun Content() {
        BaseTheme {
            BucketCircularProgressBar()
        }
    }

}
package com.zinc.berrybucket.compose.ui.component

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import com.zinc.berrybucket.compose.theme.BaseTheme

class ProfileCircularProgressBarWidget @JvmOverloads constructor(
        private val imageUrl: String,
        private val percentage: Float,
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
            ProfileCircularProgressBar(
                    percentage = percentage,
                    profileImageUrl = imageUrl
            )
        }
    }

}
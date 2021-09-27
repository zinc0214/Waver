package com.zinc.mybury_2.compose.ui.component

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import com.zinc.mybury_2.compose.theme.BaseTheme
import com.zinc.mybury_2.model.BucketProgressState
import com.zinc.mybury_2.model.BucketType

class BucketCircularProgressBarWidget @JvmOverloads constructor(
    private var animFinishEvent: (BucketProgressState) -> Unit,
    private val bucketType: BucketType,
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AbstractComposeView(context, attrs, defStyle) {

    @Composable
    override fun Content() {
        BaseTheme {
            BucketCircularProgressBar(animFinishEvent, bucketType)
        }
    }

}
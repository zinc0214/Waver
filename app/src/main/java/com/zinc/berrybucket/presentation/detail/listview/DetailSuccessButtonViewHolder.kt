package com.zinc.berrybucket.presentation.detail.listview

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.compose.theme.BaseTheme
import com.zinc.berrybucket.compose.ui.detail.DetailSuccessButtonView
import com.zinc.berrybucket.databinding.WidgetComposeViewBinding

class DetailSuccessButtonViewHolder(private val binding: WidgetComposeViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(successClicked: () -> Unit) {
        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                BaseTheme {
                    DetailSuccessButtonView(
                        successClicked = successClicked
                    )
                }
            }
        }
    }
}
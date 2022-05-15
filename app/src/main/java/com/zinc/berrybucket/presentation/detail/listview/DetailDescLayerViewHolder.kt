package com.zinc.berrybucket.presentation.detail.listview

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.compose.theme.BaseTheme
import com.zinc.berrybucket.compose.ui.detail.DetailDescLayer
import com.zinc.berrybucket.databinding.WidgetComposeViewBinding
import com.zinc.berrybucket.model.CommonDetailDescInfo

class DetailDescLayerViewHolder(private val binding: WidgetComposeViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(commonDetailDescInfo: CommonDetailDescInfo) {
        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                BaseTheme {
                    DetailDescLayer(
                        detailDescInfo = commonDetailDescInfo
                    )
                }
            }
        }
    }
}
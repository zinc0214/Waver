package com.zinc.berrybucket.presentation.detail.listview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.compose.theme.BaseTheme
import com.zinc.berrybucket.compose.ui.component.ImageViewPagerOutSideIndicator
import com.zinc.berrybucket.databinding.WidgetComposeViewBinding
import com.zinc.berrybucket.model.ImageInfo

class DetailCloseImageLayerViewHolder(private val binding: WidgetComposeViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(imageInfo: ImageInfo) {
        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                BaseTheme {
                    ImageViewPagerOutSideIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        imageList = imageInfo.imageList
                    )
                }
            }
        }
    }
}
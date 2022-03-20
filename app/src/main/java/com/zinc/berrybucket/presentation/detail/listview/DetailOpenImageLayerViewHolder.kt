package com.zinc.berrybucket.presentation.detail.listview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.compose.theme.BaseTheme
import com.zinc.berrybucket.compose.ui.component.ImageViewPagerInsideIndicator
import com.zinc.berrybucket.databinding.WidgetComposeViewBinding
import com.zinc.berrybucket.model.ImageInfo

class DetailOpenImageLayerViewHolder(private val binding: WidgetComposeViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(imageInfo: ImageInfo) {
        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                BaseTheme {
                    ImageViewPagerInsideIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        indicatorModifier = Modifier.padding(bottom = 16.dp, end = 16.dp),
                        imageList = imageInfo.imageList
                    )
                }
            }
        }
    }
}
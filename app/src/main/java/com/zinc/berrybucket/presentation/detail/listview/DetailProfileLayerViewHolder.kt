package com.zinc.berrybucket.presentation.detail.listview

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.compose.theme.BaseTheme
import com.zinc.berrybucket.compose.ui.detail.DetailProfileLayer
import com.zinc.berrybucket.databinding.WidgetComposeViewBinding
import com.zinc.berrybucket.model.ProfileInfo

class DetailProfileLayerViewHolder(private val binding: WidgetComposeViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(profileInfo: ProfileInfo) {
        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                BaseTheme {
                    DetailProfileLayer(
                        profileInfo = profileInfo
                    )
                }
            }
        }
    }
}
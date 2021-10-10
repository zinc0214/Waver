package com.zinc.berrybucket.presentation.feed.view.recyclerView.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.databinding.WidgetFeedRecommendBinding

class RecommendItemViewHolder(private val binding: WidgetFeedRecommendBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(text: String, isCenter: Boolean) {
        binding.apply {
            this.text = text
            this.isCenter = isCenter
        }
    }
}
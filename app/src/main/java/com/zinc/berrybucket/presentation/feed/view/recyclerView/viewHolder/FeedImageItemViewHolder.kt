package com.zinc.berrybucket.presentation.feed.view.recyclerView.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zinc.berrybucket.R
import com.zinc.berrybucket.databinding.WidgetFeedImageBinding

class FeedImageItemViewHolder(private val binding: WidgetFeedImageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(url: String) {
        binding.apply {
            val context = binding.root.context
            Glide.with(context).load(R.drawable.kakao).into(imageView)
        }
    }
}



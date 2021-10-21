package com.zinc.berrybucket.presentation.feed.view.recyclerView.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.databinding.WidgetFeedImageBinding
import com.zinc.berrybucket.presentation.feed.view.recyclerView.viewHolder.FeedImageItemViewHolder

class FeedImageViewPagerAdapter(private val imageList: List<String>) :
    RecyclerView.Adapter<FeedImageItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedImageItemViewHolder {
        return FeedImageItemViewHolder(
            WidgetFeedImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holderText: FeedImageItemViewHolder, position: Int) {
        holderText.bind(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

}
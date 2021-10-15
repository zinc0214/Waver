package com.zinc.berrybucket.presentation.feed.view.recyclerView.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.databinding.WidgetFeedContentBinding
import com.zinc.berrybucket.model.FeedInfo
import com.zinc.berrybucket.presentation.feed.view.recyclerView.viewHolder.FeedContentItemViewHolder

class FeedContentItemAdapter(
    private val contentList: List<FeedInfo>
) :
    RecyclerView.Adapter<FeedContentItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedContentItemViewHolder {
        return FeedContentItemViewHolder(
            WidgetFeedContentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holderText: FeedContentItemViewHolder, position: Int) {
        holderText.bind(contentList[position])
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

}
package com.zinc.berrybucket.presentation.feed.view.recyclerView.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.databinding.WidgetFeedRecommendBinding
import com.zinc.berrybucket.presentation.feed.view.recyclerView.viewHolder.RecommendItemViewHolder

class RecommendItemAdapter(
    private val list: List<String>
) :
    RecyclerView.Adapter<RecommendItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendItemViewHolder {
        return RecommendItemViewHolder(
            WidgetFeedRecommendBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holderText: RecommendItemViewHolder, position: Int) {
        holderText.bind(list[position], (position + 1) / 3 != 0)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
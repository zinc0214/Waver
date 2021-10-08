package com.zinc.berrybucket.presentation.my.view.recyclerView.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.databinding.WidgetCardTextDdayBinding
import com.zinc.berrybucket.presentation.my.view.recyclerView.viewHolder.CardTextDdayViewHolder
import com.zinc.data.models.BucketInfoSimple

class DdayBucketAdapter(
    private val list: List<BucketInfoSimple>,
    private val loadingFinished: () -> Unit
) :
    RecyclerView.Adapter<CardTextDdayViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTextDdayViewHolder {
        return CardTextDdayViewHolder(
            WidgetCardTextDdayBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holderText: CardTextDdayViewHolder, position: Int) {
        holderText.bind(list[position], loadingFinished)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
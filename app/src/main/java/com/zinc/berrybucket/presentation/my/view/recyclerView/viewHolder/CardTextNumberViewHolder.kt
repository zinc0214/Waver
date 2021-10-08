package com.zinc.berrybucket.presentation.my.view.recyclerView.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.databinding.WidgetCardTextNumberBinding
import com.zinc.data.models.Category

class CardTextNumberViewHolder(private val binding: WidgetCardTextNumberBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(info: Category, categoryClicked: () -> Unit) {
        binding.apply {
            this.info = info
            cardTextNumberView.setOnClickListener {
                categoryClicked.invoke()
            }
        }
    }

}
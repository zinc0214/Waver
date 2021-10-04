package com.zinc.mybury_2.presentation.my.view.recyclerView.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.zinc.data.models.Category
import com.zinc.mybury_2.databinding.WidgetCardTextNumberBinding

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
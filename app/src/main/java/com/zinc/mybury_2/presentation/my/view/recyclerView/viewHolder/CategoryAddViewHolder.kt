package com.zinc.mybury_2.presentation.my.view.recyclerView.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.zinc.mybury_2.databinding.WidgetAddCategoryBinding

class CategoryAddViewHolder(private val binding: WidgetAddCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(clicked: () -> Unit) {
        binding.addButtonClicked = View.OnClickListener { clicked.invoke() }
    }
}
package com.zinc.mybury_2.presentation.my.view.recyclerView.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zinc.data.models.Category
import com.zinc.mybury_2.databinding.WidgetCardTextNumberBinding
import com.zinc.mybury_2.presentation.my.view.recyclerView.CardTextNumberViewHolder

class CategoryAdapter(
    private val list: List<Category>,
    private val categoryClicked: () -> Unit
) :
    RecyclerView.Adapter<CardTextNumberViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTextNumberViewHolder {
        return CardTextNumberViewHolder(
            WidgetCardTextNumberBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holderText: CardTextNumberViewHolder, position: Int) {
        holderText.bind(list[position], categoryClicked)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
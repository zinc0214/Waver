package com.zinc.mybury_2.presentation.my.view.recyclerView.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zinc.data.models.Category
import com.zinc.mybury_2.databinding.WidgetAddCategoryBinding
import com.zinc.mybury_2.databinding.WidgetCardTextNumberBinding
import com.zinc.mybury_2.presentation.my.view.recyclerView.viewHolder.CardTextNumberViewHolder
import com.zinc.mybury_2.presentation.my.view.recyclerView.viewHolder.CategoryAddViewHolder

interface ClickListener {
    fun categorySelected()
    fun addCategoryClicked()
}

class CategoryAdapter(
    private val list: List<Category>,
    private val clickListener: ClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == CategoryViewType.Default.type) {
            CardTextNumberViewHolder(
                WidgetCardTextNumberBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        } else {
            CategoryAddViewHolder(
                WidgetAddCategoryBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CardTextNumberViewHolder) {
            holder.bind(list[position]) {
                clickListener.categorySelected()
            }
        } else if (holder is CategoryAddViewHolder) {
            holder.bind {
                clickListener.addCategoryClicked()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size) CategoryViewType.Last.type
        else CategoryViewType.Default.type
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    enum class CategoryViewType(val type: Int) {
        Default(0), Last(1);
    }
}
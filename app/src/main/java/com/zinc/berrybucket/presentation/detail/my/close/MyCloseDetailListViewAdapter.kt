package com.zinc.berrybucket.presentation.detail.my.close

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.zinc.berrybucket.databinding.WidgetComposeViewBinding
import com.zinc.berrybucket.model.*
import com.zinc.berrybucket.presentation.detail.listview.DetailCloseImageLayerViewHolder
import com.zinc.berrybucket.presentation.detail.listview.DetailDescLayerViewHolder
import com.zinc.berrybucket.presentation.detail.listview.DetailMemoLayerViewHolder

class MyCloseDetailListViewAdapter(
    private val successClicked: () -> Unit
) : ListAdapter<DetailDescType, ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return detailId(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> DetailCloseImageLayerViewHolder(
                WidgetComposeViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            2 -> DetailDescLayerViewHolder(
                WidgetComposeViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> DetailMemoLayerViewHolder(
                WidgetComposeViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is DetailCloseImageLayerViewHolder -> {
                holder.bind(imageInfo = getItem(position) as ImageInfo)
            }
            is DetailDescLayerViewHolder -> {
                holder.bind(commonDetailDescInfo = (getItem(position) as CloseDetailDescInfo).commonDetailDescInfo)
            }
            is DetailMemoLayerViewHolder -> {
                holder.bind(memoInfo = getItem(position) as MemoInfo)
            }
        }
    }

    fun updateItems(items: List<DetailDescType>) {
        submitList(items)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<DetailDescType>() {
            override fun areContentsTheSame(oldItem: DetailDescType, newItem: DetailDescType) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: DetailDescType, newItem: DetailDescType) =
                oldItem == newItem
        }
    }
}
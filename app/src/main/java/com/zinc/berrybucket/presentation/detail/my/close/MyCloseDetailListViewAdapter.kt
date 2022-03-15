package com.zinc.berrybucket.presentation.detail.my.close

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.zinc.berrybucket.databinding.WidgetComposeViewBinding
import com.zinc.berrybucket.model.*
import com.zinc.berrybucket.presentation.detail.listview.*

class MyCloseDetailListViewAdapter(
    private val detailInfoList: List<DetailType>
) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return detailId(detailInfoList[position])
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
                holder.bind(imageInfo = detailInfoList[position] as ImageInfo)
            }
            is DetailDescLayerViewHolder -> {
                holder.bind(commonDetailDescInfo = (detailInfoList[position] as CloseDetailDescInfo).commonDetailDescInfo)
            }
            is DetailMemoLayerViewHolder -> {
                holder.bind(memoInfo = detailInfoList[position] as MemoInfo)
            }
        }
    }

    override fun getItemCount(): Int {
        return detailInfoList.size
    }
}
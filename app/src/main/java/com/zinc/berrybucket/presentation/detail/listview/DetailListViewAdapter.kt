package com.zinc.berrybucket.presentation.detail.listview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.zinc.berrybucket.databinding.WidgetComposeViewBinding
import com.zinc.berrybucket.model.CommentInfo
import com.zinc.berrybucket.model.DetailDescInfo
import com.zinc.berrybucket.model.DetailType

class DetailListViewAdapter(
    private val detailInfoList: List<DetailType>,
    private val successClicked: () -> Unit
) :
    RecyclerView.Adapter<ViewHolder>() {

    private var isVisible = true

    override fun getItemViewType(position: Int): Int {
        val type = when (detailInfoList[position]) {
            is DetailDescInfo -> 0
            is DetailType.ButtonLayer -> 1
            is CommentInfo -> 2
            else -> 3
        }
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> DetailLayerViewHolder(
                WidgetComposeViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            1 -> DetailSuccessButtonViewHolder(
                WidgetComposeViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> DetailLayerViewHolder(
                WidgetComposeViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is DetailLayerViewHolder -> {
                holder.bind(detailDescInfo = detailInfoList[position] as DetailDescInfo)
            }
            is DetailSuccessButtonViewHolder -> {
                holder.bind(isVisible, successClicked)
            }
        }
    }

    fun updateSuccessButton(isVisible: Boolean) {
        this.isVisible = isVisible
    }

    override fun getItemCount(): Int {
        return detailInfoList.size
    }
}
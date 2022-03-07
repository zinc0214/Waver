package com.zinc.berrybucket.presentation.detail.listview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.zinc.berrybucket.databinding.WidgetComposeViewBinding
import com.zinc.berrybucket.model.*

class DetailListViewAdapter(
    private val detailInfoList: List<DetailType>,
    private val successClicked: () -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    private var isVisible = true

    override fun getItemViewType(position: Int): Int {
        return detailId(detailInfoList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> DetailProfileLayerViewHolder(
                WidgetComposeViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            1 -> DetailDescLayerViewHolder(
                WidgetComposeViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            2 -> DetailMemoLayerViewHolder(
                WidgetComposeViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            3 -> DetailCommentViewHolder(
                WidgetComposeViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> DetailSuccessButtonViewHolder(
                WidgetComposeViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is DetailProfileLayerViewHolder -> {
                holder.bind(profileInfo = detailInfoList[position] as ProfileInfo)
            }
            is DetailDescLayerViewHolder -> {
                holder.bind(detailDescInfo = detailInfoList[position] as DetailDescInfo)
            }
            is DetailMemoLayerViewHolder -> {
                holder.bind(memoInfo = detailInfoList[position] as MemoInfo)
            }
            is DetailCommentViewHolder -> {
                holder.bind(commentInfo = detailInfoList[position] as CommentInfo)
            }
            is DetailSuccessButtonViewHolder -> {
                holder.bind(isVisible, successClicked)
            }
        }
    }

    fun updateSuccessButton(isVisible: Boolean) {
        this.isVisible = isVisible
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return detailInfoList.size
    }
}
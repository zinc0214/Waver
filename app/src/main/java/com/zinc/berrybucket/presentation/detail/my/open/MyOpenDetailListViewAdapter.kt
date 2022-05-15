package com.zinc.berrybucket.presentation.detail.my.open

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.zinc.berrybucket.databinding.WidgetComposeViewBinding
import com.zinc.berrybucket.model.*
import com.zinc.berrybucket.presentation.detail.listview.*

class MyOpenDetailListViewAdapter(
    private val successClicked: () -> Unit,
    private val commentLongClicked: (String) -> Unit
) : ListAdapter<DetailDescType, ViewHolder>(diffUtil) {

    private var isVisible = true

    override fun getItemViewType(position: Int): Int {
        return detailId(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> DetailOpenImageLayerViewHolder(
                WidgetComposeViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            1 -> DetailProfileLayerViewHolder(
                WidgetComposeViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            2 -> DetailDescLayerViewHolder(
                WidgetComposeViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            3 -> DetailMemoLayerViewHolder(
                WidgetComposeViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            4 -> DetailCommentViewHolder(
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
            is DetailOpenImageLayerViewHolder -> {
                holder.bind(imageInfo = getItem(position) as ImageInfo)
            }
            is DetailProfileLayerViewHolder -> {
                holder.bind(profileInfo = getItem(position) as ProfileInfo)
            }
            is DetailDescLayerViewHolder -> {
                holder.bind(commonDetailDescInfo = getItem(position) as CommonDetailDescInfo)
            }
            is DetailMemoLayerViewHolder -> {
                holder.bind(memoInfo = getItem(position) as MemoInfo)
            }
            is DetailCommentViewHolder -> {
                holder.bind(
                    commentInfo = getItem(position) as CommentInfo,
                    commentLongClicked = commentLongClicked
                )
            }
            is DetailSuccessButtonViewHolder -> {
                holder.bind(isVisible, successClicked)
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
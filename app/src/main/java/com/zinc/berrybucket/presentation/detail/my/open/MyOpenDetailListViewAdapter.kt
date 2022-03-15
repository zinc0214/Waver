package com.zinc.berrybucket.presentation.detail.my.open

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.zinc.berrybucket.databinding.WidgetComposeViewBinding
import com.zinc.berrybucket.model.*
import com.zinc.berrybucket.presentation.detail.listview.*

class MyOpenDetailListViewAdapter(
    private val detailInfoList: List<DetailType>,
    private val successClicked: () -> Unit,
    private val commentLongClicked: (String) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    private var isVisible = true

    override fun getItemViewType(position: Int): Int {
        return detailId(detailInfoList[position])
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
                holder.bind(imageInfo = detailInfoList[position] as ImageInfo)
            }
            is DetailProfileLayerViewHolder -> {
                holder.bind(profileInfo = detailInfoList[position] as ProfileInfo)
            }
            is DetailDescLayerViewHolder -> {
                holder.bind(commonDetailDescInfo = detailInfoList[position] as CommonDetailDescInfo)
            }
            is DetailMemoLayerViewHolder -> {
                holder.bind(memoInfo = detailInfoList[position] as MemoInfo)
            }
            is DetailCommentViewHolder -> {
                holder.bind(
                    commentInfo = detailInfoList[position] as CommentInfo,
                    commentLongClicked = commentLongClicked
                )
            }
            is DetailSuccessButtonViewHolder -> {
                holder.bind(isVisible, successClicked)
            }
        }
    }

    fun updateSuccessButton(isVisible: Boolean) {
        this.isVisible = isVisible
        notifyDataSetChanged()
        //Log.e("ayhan", "succssButtonIndex :$succssButtonIndex")
        //notifyItemChanged(succssButtonIndex)
    }

    override fun getItemCount(): Int {
        return detailInfoList.size
    }
}
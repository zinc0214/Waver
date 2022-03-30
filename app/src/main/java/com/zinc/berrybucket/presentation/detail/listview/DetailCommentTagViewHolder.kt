package com.zinc.berrybucket.presentation.detail.listview

import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.databinding.ItemCommentTagBinding
import com.zinc.berrybucket.model.CommentTagInfo

class DetailCommentTagViewHolder(private val binding: ItemCommentTagBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(tagInfo: CommentTagInfo, itemSelected: (CommentTagInfo) -> Unit) {
        binding.commentTagInfo = tagInfo
        binding.setItemSelect {
            itemSelected(tagInfo)
        }
    }
}
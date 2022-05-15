package com.zinc.berrybucket.presentation.detail.listview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.zinc.berrybucket.databinding.ItemCommentTagBinding
import com.zinc.berrybucket.model.CommentTagInfo

class CommentTagListViewAdapter(
    private val itemSelected: (CommentTagInfo) -> Unit
) : ListAdapter<CommentTagInfo, DetailCommentTagViewHolder>(diffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailCommentTagViewHolder {
        return DetailCommentTagViewHolder(
            ItemCommentTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DetailCommentTagViewHolder, position: Int) {
        holder.bind(getItem(position), itemSelected)
    }

    fun updateItems(items: List<CommentTagInfo>) {
        submitList(items)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CommentTagInfo>() {
            override fun areContentsTheSame(oldItem: CommentTagInfo, newItem: CommentTagInfo) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: CommentTagInfo, newItem: CommentTagInfo) =
                oldItem == newItem
        }
    }
}
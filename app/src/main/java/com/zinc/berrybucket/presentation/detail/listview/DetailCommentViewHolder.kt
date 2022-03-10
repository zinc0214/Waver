package com.zinc.berrybucket.presentation.detail.listview

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.compose.theme.BaseTheme
import com.zinc.berrybucket.compose.ui.detail.DetailCommentLayer
import com.zinc.berrybucket.databinding.WidgetComposeViewBinding
import com.zinc.berrybucket.model.CommentInfo

class DetailCommentViewHolder(private val binding: WidgetComposeViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(commentInfo: CommentInfo, commentLongClicked: (String) -> Unit) {
        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                BaseTheme {
                    DetailCommentLayer(
                        commentInfo = commentInfo,
                        commentLongClicked = commentLongClicked
                    )
                }
            }
        }
    }
}
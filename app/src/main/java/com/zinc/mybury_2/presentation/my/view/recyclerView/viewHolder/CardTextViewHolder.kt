package com.zinc.mybury_2.presentation.my.view.recyclerView.viewHolder

import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.zinc.data.models.BucketInfoSimple
import com.zinc.mybury_2.R
import com.zinc.mybury_2.compose.ui.component.BucketCircularProgressBarWidget
import com.zinc.mybury_2.databinding.WidgetCardTextBinding
import com.zinc.mybury_2.model.BucketProgressState
import com.zinc.mybury_2.model.BucketType

class CardTextViewHolder(private val binding: WidgetCardTextBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(info: BucketInfoSimple, animFinished: () -> Unit) {
        binding.apply {
            this.info = info
            val progressBar = BucketCircularProgressBarWidget({ isFinished ->
                progressFinished(animFinished, isFinished)
            }, BucketType.BASIC, binding.root.context)
            progressBarLayout.addView(progressBar)
        }
    }

    private fun progressFinished(animFinished: () -> Unit, progressState: BucketProgressState) {
        if (progressState == BucketProgressState.STARTED) {
            animFinished.invoke()
            binding.cardTextLayout.background = AppCompatResources.getDrawable(
                binding.root.context,
                R.drawable.card_text_and_number_press_bg
            )
        } else if (progressState == BucketProgressState.BACK) {
            binding.cardTextLayout.background = null
        }
    }
}
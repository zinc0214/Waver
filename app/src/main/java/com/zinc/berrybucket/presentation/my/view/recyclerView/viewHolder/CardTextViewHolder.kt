package com.zinc.berrybucket.presentation.my.view.recyclerView.viewHolder

import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.ui.component.BucketCircularProgressBarWidget
import com.zinc.berrybucket.databinding.WidgetCardTextBinding
import com.zinc.berrybucket.model.BucketInfoSimple
import com.zinc.berrybucket.model.BucketProgressState
import com.zinc.berrybucket.model.BucketType

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
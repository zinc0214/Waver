package com.zinc.mybury_2.presentation.my.view.recyclerView

import android.animation.ObjectAnimator
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.zinc.data.models.BucketInfoSimple
import com.zinc.mybury_2.R
import com.zinc.mybury_2.compose.ui.component.BucketCircularProgressBarWidget
import com.zinc.mybury_2.databinding.WidgetCardTextDdayBinding
import com.zinc.mybury_2.model.BucketProgressState
import com.zinc.mybury_2.model.BucketType

class CardTextDdayViewHolder(private val binding: WidgetCardTextDdayBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(info: BucketInfoSimple, animFinished: () -> Unit) {
        binding.apply {
            val type = if (info.dDay!! > 0) BucketType.D_PLUS else BucketType.D_MINUS
            this.info = info
            this.type = type
            val progressBar = BucketCircularProgressBarWidget({ isFinished ->
                progressFinished(info, animFinished, isFinished)
            }, type, binding.root.context)
            progressBarLayout.addView(progressBar)
        }
    }

    private fun progressFinished(
        info: BucketInfoSimple,
        animFinished: () -> Unit,
        progressState: BucketProgressState
    ) {
        if (progressState == BucketProgressState.STARTED) {
            animFinished.invoke()
            binding.cardTextLayout.background = AppCompatResources.getDrawable(
                binding.root.context,
                R.drawable.card_text_and_number_dday_press_bg
            )

        } else if (progressState == BucketProgressState.BACK) {
            binding.cardTextLayout.background = null
            if (info.goalCount > 1) {
                info.currentCount += 1
                setProgressMax(binding.horizontalProgressBar, info.goalCount)
                setProgressAnimate(binding.horizontalProgressBar, info.currentCount)
                binding.notifyChange()
            }
        }
    }

    private fun setProgressMax(pb: ProgressBar, max: Int) {
        pb.max = max * 100
    }

    private fun setProgressAnimate(pb: ProgressBar, progressTo: Int) {
        val animation = ObjectAnimator.ofInt(
            pb, "progress",
            (progressTo - 1) * 100, progressTo * 100
        )
        animation.duration = 1000
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }
}
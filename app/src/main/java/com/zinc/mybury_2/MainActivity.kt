package com.zinc.mybury_2

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import com.zinc.mybury_2.databinding.ActivityHomeBinding


class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        val animation = ObjectAnimator.ofInt(
            binding.bucketDefault.progressBar.bucketCircularProgressBar,
            "progress",
            0,
            100
        ) // see this max value coming back here, we animate towards that value

        animation.duration = 1000 // in milliseconds

        animation.interpolator = DecelerateInterpolator()

        binding.bucketDefault.progressBar.bucketCircularProgressBar.apply {
            setOnClickListener {
                binding.bucketDefault.progressBar.bucketCircularProgressBar.progress = 100
                animation.start()
            }

        }

        animation.addUpdateListener { animation ->
            animation?.doOnEnd {
                binding.bucketDefault.progressBar.progressEndImageView.visibility = View.VISIBLE
            }
        }

    }
}

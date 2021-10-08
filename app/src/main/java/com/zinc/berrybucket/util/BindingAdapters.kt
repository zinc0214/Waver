package com.zinc.berrybucket.util

import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.BindingAdapter

object BindingAdapters {

    @JvmName("switchIsOn1")
    @BindingAdapter("app:switchIsOn")
    @JvmStatic
    fun switchIsOn(view: MotionLayout, isOn: Boolean) {
        if (isOn) {
            view.transitionToStart()
        } else {
            view.transitionToEnd()
        }
    }
}
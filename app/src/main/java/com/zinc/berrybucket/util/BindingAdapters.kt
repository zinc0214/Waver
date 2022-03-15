package com.zinc.berrybucket.util

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.BindingAdapter


object BindingAdapters {

    @JvmName("switchIsOn")
    @BindingAdapter("app:switchIsOn")
    @JvmStatic
    fun switchIsOn(view: MotionLayout, isOn: Boolean) {
        if (isOn) {
            view.transitionToStart()
        } else {
            view.transitionToEnd()
        }
    }

//    @BindingAdapter("android:layout_marginStart")
//    @JvmStatic
//    fun View.setMarginStart(margin : Float) {
//        val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
//        layoutParams.marginStart = margin.toInt()
//        this.layoutParams = layoutParams
//    }
//
//    @BindingAdapter("android:layout_marginEnd")
//    @JvmStatic
//    fun View.setMarginEnd(margin : Float) {
//        val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
//        layoutParams.marginStart = margin.toInt()
//        this.layoutParams = layoutParams
//    }
//
//    @BindingAdapter("android:layout_marginTop")
//    @JvmStatic
//    fun View.setMarginTop(margin : Float) {
//        val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
//        layoutParams.marginStart = margin.toInt()
//        this.layoutParams = layoutParams
//    }
//
//    @BindingAdapter("android:layout_marginBottom")
//    @JvmStatic
//    fun View.setMarginBottom(margin : Float) {
//        val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
//        layoutParams.marginStart = margin.toInt()
//        this.layoutParams = layoutParams
//    }

    @BindingAdapter(
        value = ["android:layout_marginStart", "android:layout_marginTop", "android:layout_marginEnd", "android:layout_marginBottom"],
        requireAll = false
    )
    @JvmStatic
    fun View.setMargin(
        startMargin: Float = 0.0f,
        topMargin: Float = 0.0f,
        endMargin: Float = 0.0f,
        bottomMargin: Float = 0.0f
    ) {
        val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(
            startMargin.toInt(),
            topMargin.toInt(),
            endMargin.toInt(),
            bottomMargin.toInt()
        )
        this.layoutParams = layoutParams
    }
}
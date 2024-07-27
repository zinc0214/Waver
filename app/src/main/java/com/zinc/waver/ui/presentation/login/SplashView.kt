package com.zinc.waver.ui.presentation.login

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.zinc.waver.databinding.LayoutSplashBinding

class SplashView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    val animFinished: () -> Unit
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: LayoutSplashBinding

    init {
        setUpViews()
    }

    private fun setUpViews() {

        binding = LayoutSplashBinding.inflate(LayoutInflater.from(context), this, true)

        loadSplashScreen()
    }

    private fun loadSplashScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            animFinished()
        }, 3000)
    }
}
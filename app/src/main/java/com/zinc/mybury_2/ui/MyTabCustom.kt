package com.zinc.mybury_2.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.forEachIndexed
import androidx.core.view.updateLayoutParams
import com.google.android.material.tabs.TabLayout
import com.zinc.mybury_2.databinding.WidgetMyTabBinding
import com.zinc.mybury_2.util.dp2px

class MyTabCustom @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var binding: WidgetMyTabBinding

    init {
        setUpViews()
    }

    private fun setUpViews() {
        binding = WidgetMyTabBinding.inflate(LayoutInflater.from(context), this, true)
    }


    fun setUpTabDesigns(tabLayout: TabLayout) {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                val tabView = MyTabCustom(tabLayout.context, null)
                tabView.setTabTitle(tab.text?.toString() ?: "")
                tabView.setTabIndicator(tabLayout)
                tab.customView = tabView
            }
        }
    }

    private fun setTabTitle(title: String) {
        binding.tabTitle.text = title
    }

    private fun setTabIndicator(tabLayout: TabLayout) {
        val tabStrip = tabLayout.getChildAt(0)
        if (tabStrip is ViewGroup) {
            val dp9 = context.dp2px(9f)
            val dp25 = context.dp2px(25f)

            tabStrip.forEachIndexed { index, tabView ->
                tabView.minimumWidth = 0
                tabView.setPadding(0, tabView.paddingTop, 0, tabView.paddingBottom)
                if (tabView.layoutParams is MarginLayoutParams) {
                    tabView.updateLayoutParams<MarginLayoutParams> {
                        when (index) {
                            0 -> {
                                marginStart = dp25
                                marginEnd = dp9
                            }
                            tabLayout.tabCount -> {
                                marginStart = dp9
                                marginEnd = dp25
                            }
                            else -> {
                                marginStart = dp9
                                marginEnd = dp9
                            }
                        }
                    }
                }
            }
            tabLayout.requestLayout()
        }
    }
}

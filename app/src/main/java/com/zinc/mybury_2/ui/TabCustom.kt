package com.zinc.mybury_2.ui

import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout

class TabCustom {

    fun wrapTabIndicatorToTitle(tabLayout: TabLayout, externalMargin: Int, internalMargin: Int) {
        val tabStrip: View = tabLayout.getChildAt(0)
        if (tabStrip is ViewGroup) {
            val tabStripGroup = tabStrip as ViewGroup
            val childCount = (tabStrip as ViewGroup).childCount
            for (i in 0 until childCount) {
                val tabView: View = tabStripGroup.getChildAt(i)
                //set minimum width to 0 for instead for small texts, indicator is not wrapped as expected
                tabView.minimumWidth = 0
                // set padding to 0 for wrapping indicator as title
                tabView.setPadding(0, tabView.paddingTop, 0, tabView.paddingBottom)
                // setting custom margin between tabs
                if (tabView.layoutParams is ViewGroup.MarginLayoutParams) {
                    val layoutParams = tabView.layoutParams as ViewGroup.MarginLayoutParams
                    when (i) {
                        0 -> {
                            // left
                            settingMargin(layoutParams, externalMargin, internalMargin)
                        }
                        childCount - 1 -> {
                            // right
                            settingMargin(layoutParams, internalMargin, externalMargin)
                        }
                        else -> {
                            // internal
                            settingMargin(layoutParams, internalMargin, internalMargin)
                        }
                    }
                }
            }
            tabLayout.requestLayout()
        }
    }

    private fun settingMargin(layoutParams: ViewGroup.MarginLayoutParams, start: Int, end: Int) {
        layoutParams.marginStart = start
        layoutParams.marginEnd = end
        layoutParams.leftMargin = start
        layoutParams.rightMargin = end
    }

}
package com.zinc.mybury_2.presentation.my.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayout
import com.zinc.data.models.MyProfileInfo
import com.zinc.data.models.MyState
import com.zinc.domain.models.BadgeType
import com.zinc.mybury_2.R
import com.zinc.mybury_2.compose.ui.component.ProfileCircularProgressBarWidget
import com.zinc.mybury_2.databinding.ActivityMyBinding


class MyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my)
        binding.apply {
            profileInfo = MyProfileInfo(
                    nickName = "Hana",
                    profileImg = "ddd",
                    badgeType = BadgeType.TRIP1,
                    titlePosition = "안녕 반가우이",
                    bio = "나는 ESFP 한아라고 불러줘?"
            )
            status = MyState(
                    followerCount = "10",
                    followingCount = "15",
                    hasAlarm = true
            )
            val imageView = ProfileCircularProgressBarWidget("www.naver.com", 0.5f, this@MyActivity)
            profileImageLayout.addView(imageView)

            wrapTabIndicatorToTitle(tabLayout, 3, 3)
        }
    }

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
                if (tabView.layoutParams is MarginLayoutParams) {
                    val layoutParams = tabView.layoutParams as MarginLayoutParams
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

    private fun settingMargin(layoutParams: MarginLayoutParams, start: Int, end: Int) {
        layoutParams.marginStart = start
        layoutParams.marginEnd = end
        layoutParams.leftMargin = start
        layoutParams.rightMargin = end
    }
}
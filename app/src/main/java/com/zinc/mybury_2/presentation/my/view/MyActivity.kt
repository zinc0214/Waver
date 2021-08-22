package com.zinc.mybury_2.presentation.my.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.zinc.data.models.MyProfileInfo
import com.zinc.data.models.MyState
import com.zinc.domain.models.BadgeType
import com.zinc.mybury_2.R
import com.zinc.mybury_2.compose.ui.component.ProfileCircularProgressBarWidget
import com.zinc.mybury_2.databinding.ActivityMyBinding
import com.zinc.mybury_2.ui.TabCustom


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

            TabCustom().wrapTabIndicatorToTitle(tabLayout, 3, 3)

        }
    }
}
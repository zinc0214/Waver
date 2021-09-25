package com.zinc.mybury_2.presentation.my.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayout
import com.zinc.data.models.BadgeType
import com.zinc.domain.models.TopProfile
import com.zinc.mybury_2.R
import com.zinc.mybury_2.compose.ui.component.ProfileCircularProgressBarWidget
import com.zinc.mybury_2.databinding.ActivityMyBinding
import com.zinc.mybury_2.presentation.my.view.fragment.AllBucketListFragment
import com.zinc.mybury_2.presentation.my.view.fragment.CategoryListFragment
import com.zinc.mybury_2.ui.MyTabCustom
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyBinding
    // private val viewModel : MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my)
        binding.apply {
            profileInfo = TopProfile(
                nickName = "Hana",
                profileImg = "ddd",
                badgeType = BadgeType.TRIP1,
                titlePosition = "안녕 반가우이",
                bio = "나는 ESFP 한아라고 불러줘?",
                followerCount = "10",
                followingCount = "15"
            )
            val imageView = ProfileCircularProgressBarWidget("www.naver.com", 0.5f, this@MyActivity)
            profileImageLayout.addView(imageView)

            val myTabView = MyTabCustom(this@MyActivity)
            myTabView.setUpTabDesigns(tabLayout)

            val allFragment = AllBucketListFragment.newInstance()
            val categoryFragment = CategoryListFragment.newInstance()

            supportFragmentManager.beginTransaction().add(R.id.frameLayout, allFragment).commit()

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    Log.e("ayhan", "tabPosition : ${tab.position}")
                    val currentFragment = if (tab.position == 0) {
                        allFragment
                    } else {
                        categoryFragment
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, currentFragment).commit()
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {

                }

                override fun onTabReselected(tab: TabLayout.Tab) {

                }

            })

        }
    }
}
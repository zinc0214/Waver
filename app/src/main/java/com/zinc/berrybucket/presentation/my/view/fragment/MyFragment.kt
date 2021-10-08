package com.zinc.berrybucket.presentation.my.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.ui.component.ProfileCircularProgressBarWidget
import com.zinc.berrybucket.databinding.FragmentMyBinding
import com.zinc.berrybucket.ui.MyTabCustom
import com.zinc.data.models.BadgeType
import com.zinc.domain.models.TopProfile

class MyFragment : Fragment() {
    private lateinit var binding: FragmentMyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpViews()
        setUpTabLayout()
    }

    private fun setUpViews() {
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
            val imageView =
                ProfileCircularProgressBarWidget("www.naver.com", 0.5f, requireContext())
            profileImageLayout.addView(imageView)
        }
    }

    private fun setUpTabLayout() {

        val myTabView = MyTabCustom(requireContext())
        myTabView.setUpTabDesigns(binding.myTabLayout)

        val allFragment = AllBucketListFragment.newInstance()
        val categoryFragment = CategoryListFragment.newInstance()
        val ddayFragment = DdayBucketListFragment.newInstance()

        childFragmentManager.beginTransaction().add(R.id.myFrameLayout, allFragment).commit()

        binding.myTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.e("ayhan", "tabPosition : ${tab.position}")
                val currentFragment = when (tab.position) {
                    0 -> {
                        allFragment
                    }
                    1 -> {
                        categoryFragment
                    }
                    else -> {
                        ddayFragment
                    }
                }
                childFragmentManager.beginTransaction()
                    .replace(R.id.myFrameLayout, currentFragment).commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = MyFragment()
    }
}
package com.zinc.mybury_2.presentation.my.view

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zinc.mybury_2.presentation.my.view.fragment.AllBucketListFragment

class MyViewPagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return AllBucketListFragment.newInstance()
    }

}
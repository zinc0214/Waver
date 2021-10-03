package com.zinc.mybury_2.presentation.my.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.zinc.mybury_2.R
import com.zinc.mybury_2.databinding.ActivityHomeBinding
import com.zinc.mybury_2.model.AllType
import com.zinc.mybury_2.presentation.my.view.fragment.AllBucketListFragment
import com.zinc.mybury_2.presentation.my.view.fragment.MyFragment


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var fragmentManager: FragmentManager
    private val myFragment = MyFragment.newInstance()
    private val allFragment = AllBucketListFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager = supportFragmentManager
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.activity = this
        setUpFragments()
        setMyFragment()

    }

    private fun setUpFragments() {
        fragmentManager.beginTransaction().add(R.id.frameLayout, myFragment).commit()
        fragmentManager.beginTransaction().add(R.id.frameLayout, allFragment).commit()

    }

    fun setMyFragment() {
        binding.selectType = AllType.MY
        fragmentManager.beginTransaction().show(myFragment).commit()
        fragmentManager.beginTransaction().hide(allFragment).commit()
    }

    fun setFeedFragment() {
        binding.selectType = AllType.FEED
        fragmentManager.beginTransaction().show(allFragment).commit()
        fragmentManager.beginTransaction().hide(myFragment).commit()
    }
}
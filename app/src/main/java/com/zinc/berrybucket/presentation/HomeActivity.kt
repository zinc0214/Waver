package com.zinc.berrybucket.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.zinc.berrybucket.R
import com.zinc.berrybucket.databinding.ActivityHomeBinding
import com.zinc.berrybucket.model.AllType
import com.zinc.berrybucket.presentation.detail.my.open.MyOpenDetailActivity
import com.zinc.berrybucket.presentation.feed.view.fragment.FeedFragment
import com.zinc.berrybucket.presentation.my.view.fragment.MyFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var fragmentManager: FragmentManager
    private lateinit var goToBucketDetail: (String) -> Unit
    private lateinit var myFragment: MyFragment
    private lateinit var feedFragment: FeedFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager = supportFragmentManager
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.activity = this
        init()
        setUpFragments()
        setMyFragment()
    }

    private fun init() {

        goToBucketDetail = {
            startActivity(Intent(this, MyOpenDetailActivity::class.java))
        }

        myFragment = MyFragment.newInstance(goToBucketDetail = { goToBucketDetail.invoke(it) })
        feedFragment = FeedFragment.newInstance()
    }

    private fun setUpFragments() {
        fragmentManager.beginTransaction().add(R.id.frameLayout, myFragment).commit()
        fragmentManager.beginTransaction().add(R.id.frameLayout, feedFragment).commit()

    }

    fun setMyFragment() {
        binding.selectType = AllType.MY
        fragmentManager.beginTransaction().show(myFragment).commit()
        fragmentManager.beginTransaction().hide(feedFragment).commit()
    }

    fun setFeedFragment() {
        binding.selectType = AllType.FEED
        fragmentManager.beginTransaction().show(feedFragment).commit()
        fragmentManager.beginTransaction().hide(myFragment).commit()
    }
}
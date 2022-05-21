package com.zinc.berrybucket.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.ui.BerryBucketApp
import com.zinc.berrybucket.databinding.ActivityHomeBinding
import com.zinc.berrybucket.model.AllType
import com.zinc.berrybucket.model.DetailType
import com.zinc.berrybucket.model.UIBucketInfoSimple
import com.zinc.berrybucket.presentation.detail.my.close.MyCloseBucketDetailActivity
import com.zinc.berrybucket.presentation.detail.my.open.MyOpenBucketDetailActivity
import com.zinc.berrybucket.presentation.my.view.fragment.MyFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var fragmentManager: FragmentManager
    private lateinit var goToBucketDetail: (UIBucketInfoSimple) -> Unit
    private lateinit var myFragment: MyFragment
    //   private lateinit var feedFragment: FeedFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This app draws behind the system bars, so we want to handle fitting system windows
        // WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            BerryBucketApp()
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        fragmentManager = supportFragmentManager
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
//        binding.activity = this
//        init()
//        setUpFragments()
//        setMyFragment()
//    }

    private fun init() {

        goToBucketDetail = {
            if (it.detailType == DetailType.MY_CLOSE) {
                startActivity(Intent(this, MyCloseBucketDetailActivity::class.java))
            } else if (it.detailType == DetailType.MY_OPEN) {
                startActivity(Intent(this, MyOpenBucketDetailActivity::class.java))
            }
        }

        myFragment = MyFragment.newInstance(goToBucketDetail = { goToBucketDetail.invoke(it) })
        //feedFragment = FeedFragment.newInstance()
    }

    private fun setUpFragments() {
        fragmentManager.beginTransaction().add(R.id.frameLayout, myFragment).commit()
        //  fragmentManager.beginTransaction().add(R.id.frameLayout, feedFragment).commit()

    }

    fun setMyFragment() {
        binding.selectType = AllType.MY
        fragmentManager.beginTransaction().show(myFragment).commit()
        //      fragmentManager.beginTransaction().hide(feedFragment).commit()
    }

    fun setFeedFragment() {
        binding.selectType = AllType.FEED
//        fragmentManager.beginTransaction().show(feedFragment).commit()
        fragmentManager.beginTransaction().hide(myFragment).commit()
    }
}
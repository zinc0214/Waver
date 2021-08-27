package com.zinc.mybury_2.presentation.my.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.zinc.data.models.BucketInfoSimple
import com.zinc.mybury_2.R
import com.zinc.mybury_2.databinding.FragmentAllBucketListBinding
import com.zinc.mybury_2.model.AllBucketList

class AllBucketListFragment : Fragment() {

    private lateinit var binding: FragmentAllBucketListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_bucket_list, container, false)
        binding.info = AllBucketList(
                proceedingBucketCount = "11",
                succeedBucketCount = "20",
                bucketList = listOf(
                        BucketInfoSimple(
                                id = "1",
                                title = "아이스크림을 먹을테야",
                                currentCount = 1
                        )
                )
        )
        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance(info: AllBucketList) = AllBucketListFragment().apply {

        }
    }
}
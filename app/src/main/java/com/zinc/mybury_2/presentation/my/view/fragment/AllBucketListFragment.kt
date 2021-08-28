package com.zinc.mybury_2.presentation.my.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zinc.data.models.BucketInfoSimple
import com.zinc.mybury_2.R
import com.zinc.mybury_2.databinding.FragmentAllBucketListBinding
import com.zinc.mybury_2.model.AllBucketList
import com.zinc.mybury_2.presentation.my.view.recyclerView.AllBucketAdapter

class AllBucketListFragment : Fragment() {

    private lateinit var binding: FragmentAllBucketListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_all_bucket_list, container, false)
        val allInfo = loadAllBucket()
        binding.info = allInfo

        val adapter = AllBucketAdapter(allInfo.bucketList) {
            Toast.makeText(requireContext(), "END", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        return binding.root
    }

    private fun loadAllBucket() = AllBucketList(
        proceedingBucketCount = "11",
        succeedBucketCount = "20",
        bucketList = listOf(
            BucketInfoSimple(
                id = "1",
                title = "아이스크림을 먹을테야",
                currentCount = 1
            ),
            BucketInfoSimple(
                id = "2",
                title = "제주도 여행을 갈거란 말이야",
                currentCount = 1
            ),
            BucketInfoSimple(
                id = "1",
                title = "아이스크림을 먹을테야",
                currentCount = 1
            )
        )
    )

    companion object {
        @JvmStatic
        fun newInstance() = AllBucketListFragment()
    }
}
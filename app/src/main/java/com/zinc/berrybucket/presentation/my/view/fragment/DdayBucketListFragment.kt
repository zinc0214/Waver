package com.zinc.berrybucket.presentation.my.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zinc.berrybucket.R
import com.zinc.berrybucket.databinding.FragmentDdayBucketListBinding
import com.zinc.berrybucket.model.AllBucketList
import com.zinc.berrybucket.model.BucketInfoSimple
import com.zinc.berrybucket.presentation.my.view.recyclerView.adapter.DdayBucketAdapter

class DdayBucketListFragment : Fragment() {

    private lateinit var binding: FragmentDdayBucketListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dday_bucket_list, container, false)
        val allInfo = loadAllBucket()
        binding.info = allInfo

        val adapter = DdayBucketAdapter(allInfo.bucketList) {
            Toast.makeText(requireContext(), "END", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.filterImageView.setOnClickListener { showFilterDialog() }
        return binding.root
    }

    private fun loadAllBucket() = AllBucketList(
        proceedingBucketCount = "11",
        succeedBucketCount = "20",
        bucketList = listOf(
            BucketInfoSimple(
                id = "3",
                title = "Dday가 있는 애 1, 아이스크림 냠냠 후루룹쨥짭",
                currentCount = 1,
                goalCount = 10,
                dDay = 0
            ),
            BucketInfoSimple(
                id = "3",
                title = "Dday가 있는 애222",
                currentCount = 2,
                goalCount = 10,
                dDay = 20
            ),
            BucketInfoSimple(
                id = "3",
                title = "Dday가 있는 애22233",
                currentCount = 5,
                goalCount = 10,
                dDay = -10
            )
        )
    )

    private fun showFilterDialog() {
        DdayFilterBottomSheetFragment().show(
            parentFragmentManager,
            "DdayFilterBottomSheetFragment"
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = DdayBucketListFragment()
    }
}
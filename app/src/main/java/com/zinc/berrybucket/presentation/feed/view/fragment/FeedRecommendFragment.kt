package com.zinc.berrybucket.presentation.feed.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.zinc.berrybucket.R
import com.zinc.berrybucket.databinding.FragmentFeedRecommendBinding
import com.zinc.berrybucket.presentation.feed.view.recyclerView.adapter.RecommendItemAdapter

class FeedRecommendFragment : Fragment() {

    private lateinit var binding: FragmentFeedRecommendBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_feed_recommend, container, false)
        setAdapter()
        return binding.root
    }

    private fun setAdapter() {
        FlexboxLayoutManager(requireContext()).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.CENTER
        }.apply {
            val adapter = RecommendItemAdapter(listOf("여행", "제주도", "맛집탐방", "넷플릭스", "데이트", "뿅뿅짠짠"))
            binding.recommendItemList.layoutManager = this
            binding.recommendItemList.adapter = adapter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FeedRecommendFragment()
    }
}
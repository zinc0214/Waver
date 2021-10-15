package com.zinc.berrybucket.presentation.feed.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.zinc.berrybucket.R
import com.zinc.berrybucket.databinding.FragmentFeedBinding
import com.zinc.berrybucket.model.FeedInfo
import com.zinc.berrybucket.presentation.feed.view.recyclerView.adapter.FeedContentItemAdapter
import com.zinc.berrybucket.presentation.feed.view.recyclerView.adapter.RecommendItemAdapter

class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false)
        setUpViews()
        setRecommendAdapter()
        setContentAdapter()
        return binding.root
    }

    private fun setUpViews() {
        binding.recommendButton.setOnClickListener {
            binding.feedContentLayout.visibility = View.VISIBLE
            binding.recommendLayout.visibility = View.GONE
        }
    }

    private fun setRecommendAdapter() {
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

    private fun setContentAdapter() {
        val adapter = FeedContentItemAdapter(loadMockData())
        binding.feedContentListView.layoutManager = LinearLayoutManager(requireContext())
        binding.feedContentListView.adapter = adapter
    }

    private fun loadMockData(): List<FeedInfo> {
        return listOf(
            FeedInfo(
                "멋쟁이 여행가",
                "한아크크룽삐옹",
                true,
                "제주도를 10번은 여행을 하고 말테양",
                false,
                "10",
                "5",
                false
            ),
            FeedInfo(
                "노래방에 미친",
                "가수팝수 구텐탁",
                false,
                "노래방에서 노래 불러서 100점을 맞아버릴것이다",
                false,
                "100",
                "50",
                false
            ),
            FeedInfo(
                "노래방에 미친",
                "나는야 주스될거야 나는야 케첩될거야 나는야 춤을 출꺼야",
                true,
                "멋쟁이 토마토, 토마토!",
                true,
                "100",
                "50",
                true
            ),
            FeedInfo(
                "노래방에 미친",
                "나는야 주스될거야 나는야 케첩될거야 나는야 춤을 출꺼야",
                true,
                "멋쟁이 토마토, 토마토!",
                true,
                "100",
                "50",
                true
            ),
            FeedInfo(
                "노래방에 미친",
                "나는야 주스될거야 나는야 케첩될거야 나는야 춤을 출꺼야",
                true,
                "멋쟁이 토마토, 토마토!",
                true,
                "100",
                "50",
                true
            )
        )

    }

    companion object {
        @JvmStatic
        fun newInstance() = FeedFragment()
    }
}
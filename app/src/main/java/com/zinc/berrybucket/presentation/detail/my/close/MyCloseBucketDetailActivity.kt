package com.zinc.berrybucket.presentation.detail.my.close

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.R
import com.zinc.berrybucket.databinding.ActivityMyCloseBucketDetailBinding
import com.zinc.berrybucket.model.CloseDetailDescInfo
import com.zinc.berrybucket.model.DetailDescType
import com.zinc.berrybucket.model.DetailType
import com.zinc.berrybucket.presentation.detail.DetailOptionDialogFragment
import com.zinc.berrybucket.presentation.detail.DetailViewModel
import com.zinc.berrybucket.util.nonNullObserve
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCloseBucketDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyCloseBucketDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    private lateinit var detailListAdapter: MyCloseDetailListViewAdapter
    private var isCountBucket = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_close_bucket_detail)
        setUpViewModels()
    }

    private fun setUpViewModels() {
        viewModel.bucketDetailInfo.nonNullObserve(this) {
            setUpViews(it)
        }

        viewModel.getBucketDetail("close")
    }

    private fun setUpViews(detailList: List<DetailDescType>) {
        setUpScrollChangedListener(detailList)

        detailListAdapter = MyCloseDetailListViewAdapter {
            // TODO = click event
        }.apply {
            updateItems(detailList)
        }
        binding.detailListView.adapter = detailListAdapter

        val descInfo = detailList.first { it is CloseDetailDescInfo } as CloseDetailDescInfo
        binding.userCount = descInfo.userCount.toString()
        binding.goalCount = descInfo.goalCount.toString()
        isCountBucket = descInfo.goalCount > 1
        binding.isCountBucket = isCountBucket
        binding.optionButton.setOnClickListener { showDetailOptionPopup() }
    }

    private fun setUpScrollChangedListener(detailList: List<DetailDescType>) {
        val lastIndex = detailList.lastIndex

        binding.apply {
            detailListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastCompleteVisible = layoutManager.findLastCompletelyVisibleItemPosition()

                    // 하단 댓글 입력 버튼이 노출되어야 하는 경우
                    val isScrollFinished = lastCompleteVisible >= lastIndex
                    if (isScrollFinished) {
                        binding.successButtonsLayout.setBackgroundResource(R.drawable.detail_success_button_full_background)
                    } else {
                        binding.successButtonsLayout.setBackgroundResource(R.drawable.detail_success_button_background)
                    }
                    binding.isScrollFinished = isScrollFinished
                }
            })
        }
    }

    private fun showDetailOptionPopup() {
        DetailOptionDialogFragment().apply {
            setDetailType(DetailType.MY_CLOSE)
        }.show(supportFragmentManager, "showPopup")
    }


}
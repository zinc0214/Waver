package com.zinc.berrybucket.presentation.detail.my.close

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.R
import com.zinc.berrybucket.databinding.ActivityMyCloseBucketDetailBinding
import com.zinc.berrybucket.model.*
import com.zinc.berrybucket.presentation.detail.DetailOptionDialogFragment
import com.zinc.berrybucket.presentation.detail.DetailViewModel


class MyCloseBucketDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyCloseBucketDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    private lateinit var detailListAdapter: MyCloseDetailListViewAdapter
    private var isCountBucket = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_close_bucket_detail)
        setUpViews()
        setUpScrollChangedListener(detailList)
    }

    private fun setUpViews() {
        detailListAdapter = MyCloseDetailListViewAdapter(detailList)
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

    private val detailList = listOf(
        CloseDetailDescInfo(
            CommonDetailDescInfo(
                dDay = "D+201",
                tagList = listOf("여행", "강남"),
                title = "가나다라마바사",
            ),
            goalCount = 1,
            userCount = 0
        ),
        ImageInfo(
            imageList = listOf("A", "B", "C")
        ),
        MemoInfo(
            memo = "▶ 첫째날\n" +
                    "도두해안도로 - 도두봉키세스존 - 이호테우해변 - 오설록티뮤지엄 \n" +
                    "\n" +
                    "▶ 둘째날\n" +
                    " 쇠소깍 - 크엉해안경승지 - 이승악오름\n " +
                    "▶ 첫째날\n" +
                    "도두해안도로 - 도두봉키세스존 - 이호테우해변 - 오설록티뮤지엄 \n" +
                    "\n" +
                    "▶ 둘째날\n" +
                    " 쇠소깍 - 크엉해안경승지 - 이승악오름\n" + "▶ 첫째날\n" +
                    "도두해안도로 - 도두봉키세스존 - 이호테우해변 - 오설록티뮤지엄 \n" +
                    "\n" +
                    "▶ 둘째날\n" +
                    " 쇠소깍 - 크엉해안경승지 - 이승악오름\n" +
                    "▶ 첫째날\n" +
                    "도두해안도로 - 도두봉키세스존 - 이호테우해변 - 오설록티뮤지엄 \n" +
                    "\n" +
                    "▶ 둘째날\n" +
                    " 쇠소깍 - 크엉해안경승지 - 이승악오름\n"
        )
    )
}
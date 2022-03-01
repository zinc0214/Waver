package com.zinc.berrybucket.presentation.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.BaseTheme
import com.zinc.berrybucket.compose.ui.component.ImageViewPagerInsideIndicator
import com.zinc.berrybucket.databinding.FragmentBucketDetailBinding
import com.zinc.berrybucket.model.*
import com.zinc.berrybucket.presentation.detail.listview.DetailListViewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: FragmentBucketDetailBinding
    private lateinit var detailListAdapter: DetailListViewAdapter
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_bucket_detail)
        setUpViews()
    }

    private fun setUpViews() {
        binding.apply {
            imageComposeView.apply {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )
                setContent {
                    BaseTheme {
                        ImageViewPagerInsideIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            imageList = listOf("A", "B", "C", "D")
                        )
                    }
                }
            }

            detailListAdapter = DetailListViewAdapter(detailList,
                successClicked = {
                    // Success Button Clicked!
                })

            detailListView.adapter = detailListAdapter
            matchTabAndScrollPosition()

        }
    }

    private fun matchTabAndScrollPosition() {

        val itemLastIndex = detailList.lastIndex
        var isToolbarShown = false

        binding.apply {

            detailListView.setOnScrollChangeListener { _, _, scrollY, _, _ ->

                // User scrolled past image to height of toolbar and the title text is
                // underneath the toolbar, so the toolbar should be shown.
                val shouldShowToolbar = scrollY > toolbar.height

                // The new state of the toolbar differs from the previous state; update
                // appbar and toolbar attributes.
                if (isToolbarShown != shouldShowToolbar) {
                    isToolbarShown = shouldShowToolbar

                    // Use shadow animator to add elevation if toolbar is shown
                    appbar.isActivated = shouldShowToolbar

                }
            }

            detailListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                    val first = layoutManager.findFirstVisibleItemPosition()
                    val last = layoutManager.findLastVisibleItemPosition()
                    val comFirst =
                        layoutManager.findFirstCompletelyVisibleItemPosition()
                    val comLast =
                        layoutManager.findLastCompletelyVisibleItemPosition()

                    Log.e("ayhan", "lastVisible 2 : $first $last $comFirst $comLast")

                    // 현재 뷰에서 처음에 보이는 것이 완료버튼인 경우
                    if (comFirst >= 1) {
                        detailListAdapter.updateSuccessButton(true)
                        successButton.visibility = View.GONE
                        commentEditLayout.visibility = View.VISIBLE
                    } else {
                        detailListAdapter.updateSuccessButton(false)
                        successButton.visibility = View.VISIBLE
                        commentEditLayout.visibility = View.GONE
                    }
                }
            })
        }
    }

    private val detailList = listOf(
        DetailDescInfo(
            detailProfileInfo = ProfileInfo(
                profileImage = "",
                badgeImage = "",
                titlePosition = "멋쟁이 여행가",
                nickName = "한아크크룽삐옹"
            ),
            dDay = "D+201",
            tagList = listOf("여행", "강남"),
            title = "가나다라마바사",
            memo = "▶ 첫째날\n" +
                    "도두해안도로 - 도두봉키세스존 - 이호테우해변 - 오설록티뮤지엄 \n" +
                    "\n" +
                    "▶ 둘째날\n" +
                    " 쇠소깍 - 크엉해안경승지 - 이승악오름\n" +
                    "\n" +
                    "\u200B▶ 셋째날\n" +
                    " 송당무끈모루 - 안돌오름(비밀의숲)\n" +
                    "\u200B\n" +
                    "▶ 넷째날\n" +
                    " 하도미술관 - 세화해변 - 세화소품샵 - 보일꽃\n" +
                    "\n" +
                    "▶ 다섯째날\n" +
                    " 하도미술관 - 세화해변 - 세화소품샵 - 보일꽃"
        ),
        DetailType.ButtonLayer,
        CommentInfo(
            commentCount = "10",
            listOf(
                Commenter(
                    "A", "아연이 내꺼지 너무너무 이쁘지", "@귀염둥이 이명선 베리버킷 댓글입니다.\n" +
                            "베리버킷 댓글입니다."
                ),
                Commenter(
                    "B",
                    "Contrary to popular belief",
                    "Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, "
                ),
            )
        )
    )
}

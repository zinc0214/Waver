package com.zinc.berrybucket.presentation.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.BaseTheme
import com.zinc.berrybucket.compose.ui.component.ImageViewPagerInsideIndicator
import com.zinc.berrybucket.databinding.FragmentBucketDetailBinding
import com.zinc.berrybucket.model.DetailDescInfo
import com.zinc.berrybucket.model.DetailType
import com.zinc.berrybucket.model.ProfileInfo
import com.zinc.berrybucket.presentation.detail.listview.DetailListViewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: FragmentBucketDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_bucket_detail)
        setUpViews()
    }

    private fun setUpViews() {

        var isToolbarShown = false

        binding.apply {
            // scroll change listener begins at Y = 0 when image is fully collapsed
            scrollView.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->

                    // User scrolled past image to height of toolbar and the title text is
                    // underneath the toolbar, so the toolbar should be shown.
                    val shouldShowToolbar = scrollY > toolbar.height

                    // The new state of the toolbar differs from the previous state; update
                    // appbar and toolbar attributes.
                    if (isToolbarShown != shouldShowToolbar) {
                        isToolbarShown = shouldShowToolbar

                        // Use shadow animator to add elevation if toolbar is shown
                        appbar.isActivated = shouldShowToolbar

                        // Show the plant name if toolbar is shown
                        toolbarLayout.isTitleEnabled = shouldShowToolbar
                    }
                }
            )

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

            detailListView.adapter = DetailListViewAdapter(
                detailList,
                successClicked = {
                    // Success Button Clicked!
                }
            )

            matchTabAndScrollPosition()

        }
    }

    private fun matchTabAndScrollPosition() {
        val layoutManager = binding.detailListView.layoutManager as LinearLayoutManager
        val itemLastIndex = detailList.lastIndex

        binding.apply {

            detailListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                val firstPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                val lastPosition = layoutManager.findLastCompletelyVisibleItemPosition()

                val matchPosition = when (lastPosition) {
                    itemLastIndex -> {
                        lastPosition
                    }
                    else -> firstPosition
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
        DetailType.ButtonLayer
    )
}

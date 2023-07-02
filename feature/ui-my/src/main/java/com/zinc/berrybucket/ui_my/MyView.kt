package com.zinc.berrybucket.ui_my

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.zinc.berrybucket.model.BucketSelected
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.ui_my.databinding.LayoutMyViewBinding
import com.zinc.berrybucket.ui_my.viewModel.MyViewModel
import com.zinc.domain.models.TopProfile
import kotlinx.coroutines.CoroutineScope

class MyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var binding: LayoutMyViewBinding

    init {
        setUpViews()
    }

    private fun setUpViews() {
        binding = LayoutMyViewBinding.inflate(LayoutInflater.from(context), this, true)
        binding.profileComposeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
        }
        binding.tabComposeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
        }
        binding.pagerComposeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
        }
    }

    fun setProfileInfo(profileInfo: TopProfile?, alarmClicked: () -> Unit) {
        binding.profileComposeView.setContent {
            MyTopLayer(profileInfo = profileInfo, alarmClicked = { alarmClicked() })
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    fun setTabView(
        tabItems: Array<MyTabType>,
        pagerState: PagerState,
        viewModel: MyViewModel,
        coroutineScope: CoroutineScope,
        onBucketSelected: (BucketSelected) -> Unit,
        bottomSheetClicked: (BottomSheetScreenType) -> Unit,
        addNewCategory: () -> Unit,
        goToCategoryEdit: () -> Unit,
        nestedScrollInterop: NestedScrollConnection
    ) {
        binding.tabComposeView.setContent {
            MyTabLayer(tabItems, pagerState, coroutineScope)
        }
        binding.pagerComposeView.setContent {
            MyViewPager(
                tabItems = tabItems,
                pagerState = pagerState,
                viewModel = viewModel,
                onBucketSelected = onBucketSelected,
                bottomSheetClicked = bottomSheetClicked,
                addNewCategory = addNewCategory,
                goToCategoryEdit = goToCategoryEdit,
                coroutineScope = coroutineScope,
                nestedScrollInterop = nestedScrollInterop
            )
        }
    }

}
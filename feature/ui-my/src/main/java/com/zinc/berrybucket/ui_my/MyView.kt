package com.zinc.berrybucket.ui_my

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.zinc.berrybucket.model.HomeItemSelected
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.ui_my.databinding.LayoutMyViewBinding
import com.zinc.berrybucket.ui_my.model.MyTopEvent
import com.zinc.berrybucket.ui_my.viewModel.MyViewModel
import com.zinc.common.models.TopProfile
import kotlinx.coroutines.CoroutineScope

// TODO : 제거예정
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

    fun setProfileInfo(profileInfo: TopProfile?, myTopEvent: (MyTopEvent) -> Unit) {
        binding.profileComposeView.setContent {
            MyTopLayer(profileInfo = profileInfo) {
                myTopEvent(it)
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    fun setTabView(
        tabItems: List<MyTabType>,
        pagerState: PagerState,
        viewModel: MyViewModel,
        coroutineScope: CoroutineScope,
        itemSelected: (HomeItemSelected) -> Unit,
        bottomSheetClicked: (BottomSheetScreenType) -> Unit,
        goToCategoryEdit: () -> Unit,
        nestedScrollInterop: NestedScrollConnection
    ) {
        binding.tabComposeView.setContent {
            MyTabLayer(tabItems, pagerState, coroutineScope)
        }
        binding.pagerComposeView.setContent {
//            MyViewPager(
//                pagerState = pagerState,
//                viewModel = viewModel,
//                itemSelected = itemSelected,
//                bottomSheetClicked = bottomSheetClicked,
//                goToCategoryEdit = goToCategoryEdit,
//                coroutineScope = coroutineScope,
//                nestedScrollInterop = nestedScrollInterop,
//                isFilterDialogShown = false
//            )
        }
    }

}
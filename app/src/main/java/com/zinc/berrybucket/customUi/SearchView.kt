package com.zinc.berrybucket.customUi

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.google.android.material.appbar.AppBarLayout
import com.zinc.berrybucket.compose.ui.search.*
import com.zinc.berrybucket.databinding.LayoutSearchTopViewBinding
import com.zinc.common.models.RecommendList
import com.zinc.common.models.SearchRecommendCategory

class SearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var binding: LayoutSearchTopViewBinding

    init {
        setUpViews()
    }

    private fun setUpViews() {
        binding = LayoutSearchTopViewBinding.inflate(LayoutInflater.from(context), this, true)

        binding.appBatLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            Log.e("ayhan", "verticallOffset : $verticalOffset")
        })

        binding.titleView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                SearchTitle()
            }
        }
        binding.searchEditTextView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
        }
        binding.keyWordRecommendView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
        }
        binding.searchDividerView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                SearchDivider()
            }
        }
        binding.listComposeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
        }
    }

    fun setComposeView(
        editViewClicked: () -> Unit
    ) {
        binding.searchEditTextView.setContent {
            SearchEditView(editViewClicked = editViewClicked)
        }
    }

    fun setRecommendView(items: List<SearchRecommendCategory>) {
        binding.keyWordRecommendView.setContent {
            SearchRecommendCategoryItemsView(items = items)
        }
    }

    fun setRecommendBucketList(list: RecommendList) {
        binding.listComposeView.setContent {
            RecommendListView(list)
        }
    }
}
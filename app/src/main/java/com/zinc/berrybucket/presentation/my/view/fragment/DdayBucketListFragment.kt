package com.zinc.berrybucket.presentation.my.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.zinc.berrybucket.model.TabType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DdayBucketListFragment : Fragment() {

    private lateinit var searchViewClicked: (TabType) -> Unit
    // private lateinit var goToBucketDetail: (BucketInfoSimple) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
//                DdayBucketLayer(dDayBucketList = loadAllBucket(), clickEvent = {
//                    when (it) {
//                        is MyClickEvent.FilterClicked -> {
//                            showFilterDialog()
//                        }
//                        is ItemClicked -> {
//                            goToBucketDetail(it.info)
//                        }
//                        is SearchClicked -> {
//                            goToSearchFragment(it.tabType)
//                        }
//                        else -> {
//                            // Do Nothing
//                        }
//                    }
//                })
            }
        }
    }

//    private fun goToSearchFragment(tabType: TabType) {
//        searchViewClicked.invoke(tabType)
//    }
//
//    private fun goToBucketDetail(info: BucketInfoSimple) {
//        goToBucketDetail.invoke(info)
//    }


    private fun showFilterDialog() {
        DdayFilterBottomSheetFragment().show(
            parentFragmentManager,
            "DdayFilterBottomSheetFragment"
        )
    }

//    companion object {
//        @JvmStatic
//        fun newInstance(
//            searchViewClicked: (TabType) -> Unit,
//            goToBucketDetail: (BucketInfoSimple) -> Unit
//        ) = DdayBucketListFragment().apply {
//            this.searchViewClicked = searchViewClicked
//            this.goToBucketDetail = goToBucketDetail
//        }
//    }
}
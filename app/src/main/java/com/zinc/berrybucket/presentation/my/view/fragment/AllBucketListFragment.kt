package com.zinc.berrybucket.presentation.my.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.zinc.berrybucket.compose.ui.my.AllBucketLayer
import com.zinc.berrybucket.model.*

class AllBucketListFragment : Fragment() {

    private lateinit var searchViewClicked: (TabType) -> Unit
    private lateinit var goToBucketDetail: (String) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AllBucketLayer(loadAllBucket()) {
                    when (it) {
                        is MyClickEvent.FilterClicked -> {
                            showFilterDialog()
                        }
                        is ItemClicked -> {
                            goToBucketDetail(it.id)
                        }
                        is SearchClicked -> {
                            goToSearchFragment(it.tabType)
                        }
                        else -> {
                            // Do Nothing
                        }
                    }
                }
            }
        }
    }

    private fun goToSearchFragment(tabType: TabType) {
        searchViewClicked.invoke(tabType)
    }

    private fun goToBucketDetail(id: String) {
        goToBucketDetail.invoke(id)
    }

    private fun showFilterDialog() {
        MyAllFilterBottomDialogFragment().show(
            parentFragmentManager,
            "MyAllFilterBottomDialogFragment"
        )
    }


    private fun loadAllBucket() = AllBucketList(
        proceedingBucketCount = "15",
        succeedBucketCount = "20",
        bucketList = listOf(
            BucketInfoSimple(
                id = "1",
                title = "아이스크림을 먹을테야 히힛",
                currentCount = 1
            ),
            BucketInfoSimple(
                id = "2",
                title = "제주도 여행을 갈거란 말이야",
                currentCount = 1
            ),
            BucketInfoSimple(
                id = "1",
                title = "아이스크림을 먹을테야",
                currentCount = 1,
                goalCount = 5
            ),
            BucketInfoSimple(
                id = "3",
                title = "Dday가 있는 애222",
                currentCount = 5,
                goalCount = 10,
                dDay = 20
            ),
            BucketInfoSimple(
                id = "3",
                title = "Dday가 있는 애22233",
                currentCount = 5,
                goalCount = 10,
                dDay = -10
            ),
            BucketInfoSimple(
                id = "3",
                title = "Dday가 있는 애22233",
                currentCount = 5,
                goalCount = 10,
                dDay = -10
            ),
            BucketInfoSimple(
                id = "3",
                title = "Dday가 있는 애22233",
                currentCount = 5,
                goalCount = 10,
                dDay = -10
            ),
            BucketInfoSimple(
                id = "3",
                title = "Dday가 있는 애22233",
                currentCount = 5,
                goalCount = 10,
                dDay = -10
            ),
            BucketInfoSimple(
                id = "3",
                title = "Dday가 있는 애22233",
                currentCount = 5,
                goalCount = 10,
                dDay = -10
            ),
            BucketInfoSimple(
                id = "3",
                title = "Dday가 있는 애22233",
                currentCount = 5,
                goalCount = 10,
                dDay = -10
            ),
            BucketInfoSimple(
                id = "3",
                title = "Dday가 있는 애22233",
                currentCount = 5,
                goalCount = 10,
                dDay = -10
            ),
            BucketInfoSimple(
                id = "3",
                title = "Dday가 있는 애22233",
                currentCount = 5,
                goalCount = 10,
                dDay = -10
            ),
            BucketInfoSimple(
                id = "3",
                title = "Dday가 있는 애22233",
                currentCount = 5,
                goalCount = 10,
                dDay = -10
            ),
            BucketInfoSimple(
                id = "3",
                title = "Dday가 있는 애22233",
                currentCount = 5,
                goalCount = 10,
                dDay = -10
            ),
            BucketInfoSimple(
                id = "3",
                title = "Dday가 있는 애22233",
                currentCount = 5,
                goalCount = 10,
                dDay = -10
            ),
            BucketInfoSimple(
                id = "3",
                title = "Dday가 있는 애22233",
                currentCount = 5,
                goalCount = 10,
                dDay = -10
            )
        )
    )


    companion object {
        @JvmStatic
        fun newInstance(searchViewClicked: (TabType) -> Unit, goToBucketDetail: (String) -> Unit) =
            AllBucketListFragment().apply {
                this.searchViewClicked = searchViewClicked
                this.goToBucketDetail = goToBucketDetail
            }
    }
}
package com.zinc.berrybucket.ui.presentation.my.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.zinc.common.models.Category

class CategoryListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                //CategoryLayer()
            }
        }
    }

    private fun loadCategory() = listOf(
        Category(
            id = 1,
            name = "여행",
            count = "20"
        ),
        Category(
            id = 1,
            name = "아주아주 맛있는 것을 먹으러 다니는 거야 냠냠쩝쩝 하면서 룰루리랄라 크크루삥봉",
            count = "10"
        ),
        Category(
            id = 1,
            name = "제주도여행을 갈거야",
            count = "3"
        )
    )

    companion object {
        @JvmStatic
        fun newInstance() = CategoryListFragment()
    }
}
package com.zinc.berrybucket.presentation.feed.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.zinc.berrybucket.compose.ui.feed.FeedKeywordsLayer
import com.zinc.berrybucket.compose.ui.feed.FeedLayer
import com.zinc.berrybucket.model.FeedInfo

class FeedFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ShowContent()
            }
        }
    }

    @Composable
    private fun ShowContent() {
        val recommendClicked = remember {
            mutableStateOf(false)
        }
        if (recommendClicked.value) {
            FeedLayer(feedList = loadMockData())
        } else {
            FeedKeywordsLayer(
                keywords = listOf(
                    "여행", "제주도", "맛집탐방", "넷플릭스", "데이트", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠",
                    "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠",
                    "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠",
                    "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠", "뿅뿅짠짠"
                ),
                recommendClicked = {
                    recommendClicked.value = true
                })
        }

    }


    private fun loadMockData(): List<FeedInfo> {
        return listOf(
            FeedInfo(
                profileImage = "",
                badgeImage = "",
                "멋쟁이 여행가",
                "한아크크룽삐옹",
                listOf("1", "2"),
                true,
                "제주도를 10번은 여행을 하고 말테양",
                false,
                "10",
                "5",
                false
            ),
            FeedInfo(
                profileImage = "",
                badgeImage = "",
                "노래방에 미친",
                "가수팝수 구텐탁",
                listOf("1", "2"),
                false,
                "노래방에서 노래 불러서 100점을 맞아버릴것이다",
                false,
                "100",
                "50",
                false
            ),
            FeedInfo(
                profileImage = "",
                badgeImage = "",
                "노래방에 미친",
                "나는야 주스될거야 나는야 케첩될거야 나는야 춤을 출꺼야",
                null,
                true,
                "멋쟁이 토마토, 토마토!",
                true,
                "100",
                "50",
                true
            ),
            FeedInfo(
                profileImage = "",
                badgeImage = "",
                "노래방에 미친",
                "나는야 주스될거야 나는야 케첩될거야 나는야 춤을 출꺼야",
                null,
                true,
                "멋쟁이 토마토, 토마토!",
                true,
                "100",
                "50",
                true
            ),
            FeedInfo(
                profileImage = "",
                badgeImage = "",
                "노래방에 미친",
                "나는야 주스될거야 나는야 케첩될거야 나는야 춤을 출꺼야",
                listOf("1", "2", "3", "4"),
                true,
                "멋쟁이 토마토, 토마토!",
                true,
                "100",
                "50",
                true
            )
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = FeedFragment()
    }
}
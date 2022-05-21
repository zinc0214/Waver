package com.zinc.berrybucket.compose.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Feed(
    onPageClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {

    val recommendClicked = remember {
        mutableStateOf(false)
    }
    if (recommendClicked.value) {
        FeedLayer(feedList = loadMockData())
    } else {
        FeedKeywordsLayer(
            keywords = listOf(
                "여행", "제주도", "맛집탐방", "넷플릭스", "데이트",
                "여행", "제주도", "맛집탐방", "넷플릭스", "데이트",
                "여행", "제주도", "맛집탐방", "넷플릭스", "데이트",
                "여행", "제주도", "맛집탐방", "넷플릭스", "데이트",
                "여행", "제주도", "맛집탐방", "넷플릭스", "데이트",
                "여행", "제주도", "맛집탐방", "넷플릭스", "데이트",
                "여행", "제주도", "맛집탐방", "넷플릭스", "데이트",
                "뿅뿅짠짠"
            ),
            recommendClicked = {
                recommendClicked.value = true
            })
    }
}
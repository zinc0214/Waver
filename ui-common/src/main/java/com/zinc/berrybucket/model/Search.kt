package com.zinc.berrybucket.model

enum class RecommendType(val title: String) {
    POPULAR(title = "popular"),
    RECOMMEND(title = "recommend");
}

enum class SearchRecommendType {
    RECENT, RECOMMEND
}
fun RecommendType.toKorean(): String {
    return when (this) {
        RecommendType.POPULAR -> {
            "인기"
        }
        RecommendType.RECOMMEND -> {
            "추천"
        }
    }
}
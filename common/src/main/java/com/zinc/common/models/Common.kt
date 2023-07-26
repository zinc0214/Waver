package com.zinc.common.models

data class CommonResponse(
    val data: Any?,
    val success: Boolean,
    val code: String,
    val message: String
)

enum class YesOrNo {
    Y, N;

    fun isYes() = this == Y
    fun isNo() = this == N
}
package com.zinc.data.model

data class CommonResponse(
    val data: Any,
    val success: Boolean,
    val code: String,
    val message: String
)
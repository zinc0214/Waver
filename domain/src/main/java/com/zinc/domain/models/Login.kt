package com.zinc.domain.models

data class CheckEmailsAlreadyUse(
    val email: String
)

data class GoogleEmailInfo(
    val email: String,
    val uid: String
)
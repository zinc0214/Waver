package com.zinc.common.models

import java.io.File

data class EmailCheckResponse(
    val data: UserId,
    val success: Boolean,
    val code: String,
    val message: String
) {
    data class UserId(
        val userId: Int
    )
}

data class JoinResponse(
    val data: JoinAccessToken?,
    val success: Boolean,
    val code: String,
    val message: String
)

data class JoinAccessToken(
    val accessToken: String,
    val refreshToken: String
)

data class JoinEmailCheck(
    val email: String
)

data class CreateProfileRequest(
    val accountType: String = "ANDROID",
    val name: String,
    val bio: String? = null,
    val profileImage: File? = null
)

data class LoginRequest(
    val email: String
)

data class RefreshTokenResponse(
    val data: RefreshAccessToken?,
    val success: Boolean,
    val code: String,
    val message: String
)

data class RefreshAccessToken(
    val accessToken: String,
    val refreshToken: String
)

package com.zinc.common.models

import java.io.File

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

data class JoinRequest(
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
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

data class CreateProfileRequest(
    val accountType: String = "ANDROID",
    val email: String,
    val name: String,
    val bio: String? = null,
    val profileImage: File? = null
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

data class LoadTokenByEmailRequest(
    val email: String
)

data class LoadTokenByEmailResponse(
    val data: AccessTokenDto,
    val success: Boolean,
    val code: String,
    val message: String
)

data class AccessTokenDto(
    val accessToken: String
)
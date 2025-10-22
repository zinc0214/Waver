package com.zinc.common.models

import java.io.File
import java.io.Serializable

data class JoinResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val data: String? // 에러 아닐 때는 수정해야 할 듯...
) : Serializable

data class JoinAccessToken(
    val accessToken: String
) : Serializable

data class CreateProfileRequest(
    val accountType: String = "ANDROID",
    val email: String,
    val uid: String,
    val name: String,
    val bio: String? = null,
    val profileImage: File? = null
) : Serializable

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
    val uid: String
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
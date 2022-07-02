package com.zinc.common.models

data class JoinResponse(
    val accessToken: String,
    val refreshToken: String
)

@kotlinx.serialization.Serializable
data class JoinRequest(
    val email: String
) : java.io.Serializable
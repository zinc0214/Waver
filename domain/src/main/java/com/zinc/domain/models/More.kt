package com.zinc.domain.models

import java.io.File

data class UpdateProfileRequest(
    val name: String,
    val bio: String,
    val image: File? = null
)
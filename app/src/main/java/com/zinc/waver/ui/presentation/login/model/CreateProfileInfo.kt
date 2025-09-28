package com.zinc.waver.ui.presentation.login.model

import java.io.File

data class CreateProfileInfo(
    val nickName: String = "",
    val bio: String = "",
    val imgPath: String? = null,
    val imgFile: File? = null
)

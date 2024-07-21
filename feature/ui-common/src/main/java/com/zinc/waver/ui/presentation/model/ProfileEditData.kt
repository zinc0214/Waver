package com.zinc.waver.ui.presentation.model

data class ProfileEditData(
    val dataType: ProfileDataType,
    val prevText: String
) {
    enum class ProfileDataType {
        NICKNAME, BIO
    }
}

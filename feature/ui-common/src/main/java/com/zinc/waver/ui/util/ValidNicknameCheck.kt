package com.zinc.waver.ui.util

fun isValidNicknameCheck(nickname: String): Boolean {
    val specialCharRegex = Regex("[^a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]")
    val hasSpecialChar = nickname.any { specialCharRegex.matches(it.toString()) }
    return !hasSpecialChar
}
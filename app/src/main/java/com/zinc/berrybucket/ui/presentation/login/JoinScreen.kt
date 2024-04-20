package com.zinc.berrybucket.ui.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun JoinScreen(
    goToMain: () -> Unit,
    goToBack: () -> Unit,
    goToLogin: (String) -> Unit
) {

    var emailLoginSucceed by remember {
        mutableStateOf(false)
    }

    val joinTryEmail: MutableState<String?> = remember { mutableStateOf(null) }

    if (emailLoginSucceed.not()) {
        JoinEmailScreen(goToNexPage = {
            joinTryEmail.value = it
            emailLoginSucceed = true
        }, goToBack = {
            goToBack()
        }, goToLogin = goToLogin)
    } else {
        JoinNickNameScreen(
            email = joinTryEmail.value.orEmpty(),
            goToMain = { goToMain() },
            goToBack = { goToBack() }
        )
    }
}


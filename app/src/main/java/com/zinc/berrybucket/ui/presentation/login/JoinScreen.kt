package com.zinc.berrybucket.ui.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.zinc.berrybucket.ui.presentation.login.model.LoginPrevData

@Composable
fun JoinScreen(
    goToMain: () -> Unit,
    goToBack: () -> Unit
) {

    var emailLoginSucceed by remember {
        mutableStateOf(false)
    }

    val loginPrevData: MutableState<LoginPrevData?> = remember { mutableStateOf(null) }

    if (emailLoginSucceed.not()) {
        JoinEmailScreen(goToNexPage = {
            loginPrevData.value = it
            emailLoginSucceed = true
        }, goToBack = {
            goToBack()
        })
    } else if (emailLoginSucceed && loginPrevData.value != null) {
        JoinNickNameScreen(
            loginPrevData = loginPrevData.value!!,
            goToMain = { goToMain() },
            goToBack = { goToBack() }
        )
    }
}


package com.zinc.waver.ui.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.zinc.waver.ui.presentation.login.model.CreateProfileInfo
import com.zinc.waver.ui.presentation.model.ActionWithActivity

@Composable
fun JoinScreen(
    goToMain: () -> Unit,
    goToBack: () -> Unit,
    goToLogin: (String) -> Unit,
    addImageAction: (ActionWithActivity.AddImage) -> Unit,
) {

    var emailLoginSucceed by remember {
        mutableStateOf(true)
    }

    var isFirstCreate by remember { mutableStateOf(false) }
    var createProfileInfo by remember { mutableStateOf(CreateProfileInfo()) }

    val joinTryEmail: MutableState<String?> = remember { mutableStateOf(null) }

    if (emailLoginSucceed.not()) {
        JoinEmailScreen(goToNexPage = {
            joinTryEmail.value = it
            emailLoginSucceed = true
        }, goToBack = {
            goToBack()
        }, goToLogin = goToLogin)
    } else if (isFirstCreate.not()) {
        JoinCreateProfile1(
            createProfileInfo = createProfileInfo,
            goToNext = { info ->
                isFirstCreate = true
                createProfileInfo = info
            },
            addImageAction = addImageAction
        )
    } else if (isFirstCreate) {
        JoinCreateProfile2(
            email = joinTryEmail.value.orEmpty(),
            createProfileInfo = createProfileInfo,
            goToMain = goToMain,
            goToBack = {
                isFirstCreate = false
            }
        )
    }
}


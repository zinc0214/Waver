package com.zinc.waver.ui.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.zinc.waver.ui.presentation.login.model.CreateProfileInfo
import com.zinc.waver.ui.presentation.model.ActionWithActivity
import com.zinc.waver.ui.presentation.model.BadgePopupInfo
import com.zinc.waver.ui.presentation.screen.badge.BadgePopupScreen

@Composable
fun JoinScreen(
    goToMain: () -> Unit,
    goToBack: () -> Unit,
    goToLogin: (String) -> Unit,
    addImageAction: (ActionWithActivity.AddImage) -> Unit,
) {

    var emailLoginSucceed by remember {
        mutableStateOf(false)
    }

    var isFirstCreate by remember { mutableStateOf(false) }
    var createProfileInfo by remember { mutableStateOf(CreateProfileInfo()) }

    val joinTryEmail: MutableState<String?> = remember { mutableStateOf(null) }

    var showBadgePopup by remember { mutableStateOf(false) }

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
            goToMain = {
                showBadgePopup = true
            },
            goToBack = {
                isFirstCreate = false
            }
        )
    }

    if (showBadgePopup) {
        BadgePopupScreen(info = BadgePopupInfo(
            badgeUrl = "https://search.yahoo.com/search?p=audire",
            badgeText = "요리",
            badgeGrade = "1"
        ), onDismissRequest = {
            showBadgePopup = false
            goToMain()
        })
    }
}


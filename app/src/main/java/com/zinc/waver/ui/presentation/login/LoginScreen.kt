package com.zinc.waver.ui.presentation.login

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zinc.waver.R
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog

@Composable
fun LoginScreen(
    retryLoginEmail: String = "",
    goToMainHome: () -> Unit,
    goToJoin: () -> Unit,
    goToFinish: () -> Unit
) {
    val viewModel: LoginViewModel = hiltViewModel()

    val canGoToMainAsState by viewModel.goToMain.observeAsState()
    val isLoginFailAsState by viewModel.loginFail.observeAsState()
    val goToJoinAsState by viewModel.needToStartJoin.observeAsState()
    val needToStartLoadTokenAsState by viewModel.needToStartLoadToken.observeAsState()

    val needToShowLoginFailDialog = remember { mutableStateOf(isLoginFailAsState) }
    val needToShowJoinDialog = remember { mutableStateOf(goToJoinAsState) }
    val currentEmail = remember { mutableStateOf(needToStartLoadTokenAsState) }
    val isAnimFinished = remember { mutableStateOf(false) }

    if (!viewModel.isLoginChecked) {
        viewModel.checkHasLoginEmail(retryLoginEmail)
    }

    LaunchedEffect(key1 = canGoToMainAsState, key2 = isAnimFinished.value) {
        if (isAnimFinished.value) {
            canGoToMainAsState?.let {
                goToMainHome()
            }
        }
    }

    LaunchedEffect(key1 = isLoginFailAsState, key2 = isAnimFinished.value) {
        if (isAnimFinished.value) {
            needToShowLoginFailDialog.value = isLoginFailAsState
        }
    }

    LaunchedEffect(key1 = goToJoinAsState, key2 = isAnimFinished.value) {
        if (isAnimFinished.value) {
            needToShowJoinDialog.value = goToJoinAsState
        }
    }

    LaunchedEffect(key1 = needToStartLoadTokenAsState) {
        needToStartLoadTokenAsState?.let {
            Log.e(
                "ayhan",
                "needToStartLoadTokenAsState : $it,  currentEmail : ${currentEmail.value}"
            )
            viewModel.loadLoginToken(it)
            currentEmail.value = it
        }
    }

    Scaffold { padding ->

        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            factory = { context ->
                SplashView(context = context, animFinished = {
                    isAnimFinished.value = true
                })
            }
        )
        if (needToShowLoginFailDialog.value == true) {
            ApiFailDialog(
                title = stringResource(id = R.string.loginFail),
                message = stringResource(id = R.string.loginRetry)
            ) {
                goToFinish()
                needToShowLoginFailDialog.value = false
            }
        }

        if (needToShowJoinDialog.value == true) {
//            CommonDialogView(
//                title = stringResource(id = R.string.notLoginedEamil),
//                message = stringResource(id = R.string.goToJoinMessage),
//                dismissAvailable = false,
//                negative = DialogButtonInfo(
//                    text = com.zinc.waver.ui_common.R.string.cancel,
//                    color = Gray10
//                ),
//                positive = DialogButtonInfo(text = R.string.goToJoin, color = Main4),
//                negativeEvent = {
//                    goToFinish()
//                    needToShowJoinDialog.value = false
//                },
//                positiveEvent = {
//                    goToJoin()
//                    needToShowJoinDialog.value = false
//                }
//            )
            goToJoin()
        }
    }
}
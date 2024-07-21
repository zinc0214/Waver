package com.zinc.waver.ui.presentation.login

import android.util.Log
import androidx.compose.foundation.layout.Column
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.waver.R
import com.zinc.waver.model.DialogButtonInfo
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.waver.ui.presentation.component.dialog.CommonDialogView

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

    if (!viewModel.isLoginChecked) {
        viewModel.checkHasLoginEmail(retryLoginEmail)
    }

    LaunchedEffect(key1 = canGoToMainAsState) {
        canGoToMainAsState?.let {
            goToMainHome()
        }
    }

    LaunchedEffect(key1 = isLoginFailAsState) {
        needToShowLoginFailDialog.value = isLoginFailAsState
    }

    LaunchedEffect(key1 = goToJoinAsState) {
        needToShowJoinDialog.value = goToJoinAsState
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
        Column(modifier = Modifier.padding(padding)) {
            MyText(text = "로그인 중 ...")
        }

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
            CommonDialogView(
                title = stringResource(id = R.string.notLoginedEamil),
                message = stringResource(id = R.string.goToJoinMessage),
                dismissAvailable = false,
                negative = DialogButtonInfo(
                    text = com.zinc.waver.ui_common.R.string.cancel,
                    color = Gray10
                ),
                positive = DialogButtonInfo(text = R.string.goToJoin, color = Main4),
                negativeEvent = {
                    goToFinish()
                    needToShowJoinDialog.value = false
                },
                positiveEvent = {
                    goToJoin()
                    needToShowJoinDialog.value = false
                }
            )
        }
    }
}
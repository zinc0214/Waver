package com.zinc.berrybucket.ui.presentation.login

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
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.DialogButtonInfo
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.berrybucket.ui.presentation.component.dialog.CommonDialogView

@Composable
fun LoginScreen(
    goToMainHome: () -> Unit,
    goToJoin: () -> Unit,
    goToFinish: () -> Unit
) {
    val viewModel: LoginViewModel = hiltViewModel()

    viewModel.checkHasLoginEmail()

    val canGoToMainAsState by viewModel.goToMain.observeAsState()
    val isLoginFailAsState by viewModel.loginFail.observeAsState()
    val goToJoinAsState by viewModel.needToStartJoin.observeAsState()

    val needToShowLoginFailDialog = remember { mutableStateOf(isLoginFailAsState) }
    val needToShowJoinDialog = remember { mutableStateOf(goToJoinAsState) }

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
            }
        }

        if (needToShowJoinDialog.value == true) {
            CommonDialogView(
                title = stringResource(id = R.string.notLoginedEamil),
                message = stringResource(id = R.string.goToJoinMessage),
                dismissAvailable = false,
                negative = DialogButtonInfo(
                    text = com.zinc.berrybucket.ui_common.R.string.cancel,
                    color = Gray10
                ),
                positive = DialogButtonInfo(text = R.string.goToJoin, color = Main4),
                negativeEvent = {
                    goToFinish()
                },
                positiveEvent = {
                    goToJoin()
                }
            )
        }
    }
}
package com.zinc.waver.ui.presentation.login

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.waver.R
import com.zinc.waver.model.DialogButtonInfo
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.theme.Gray7
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.MyTextField
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.waver.ui.presentation.component.dialog.CommonDialogView
import com.zinc.waver.ui.presentation.login.component.ProfileCreateTitle
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_common.R as CommonR


// 회원가입1 > 이메일 입력
@Composable
fun JoinEmailScreen(
    goToNexPage: (String) -> Unit,
    goToBack: () -> Unit,
    goToLogin: (String) -> Unit
) {
    val viewModel: JoinEmailViewModel = hiltViewModel()

    // 이미 이메일이 있는지?
    val checkAlreadyUsedEmailAsState by viewModel.isAlreadyUsedEmail.observeAsState()
    val isAlreadyUsedEmail = remember { mutableStateOf(false) }

    // 존재하는 이메일이 없는 경우
    val goToMakeNickNameAsState by viewModel.goToMakeNickName.observeAsState()

    // 이메일이 있는 경우, 로그인 하러 가기
    val goToLoginAsState by viewModel.goToLogin.observeAsState()

    // api 실패
    val failApiAsState by viewModel.failEmailCheck.observeAsState()
    val isFailApi = remember { mutableStateOf(false) }

    val prevLoginEmail = remember { mutableStateOf("") }

    LaunchedEffect(key1 = checkAlreadyUsedEmailAsState) {
        isAlreadyUsedEmail.value = checkAlreadyUsedEmailAsState ?: false
    }

    LaunchedEffect(key1 = goToMakeNickNameAsState) {
        goToMakeNickNameAsState?.let { data ->
            goToNexPage(data)
        }
    }

    LaunchedEffect(key1 = failApiAsState) {
        isFailApi.value = failApiAsState == true
    }

    LaunchedEffect(key1 = goToLoginAsState) {
        if (goToLoginAsState == true) {
            goToLogin(prevLoginEmail.value)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileCreateTitle(
            saveButtonEnable = prevLoginEmail.value.isNotEmpty(),
            backClicked = {
                goToBack()
            },
            saveClicked = {
                viewModel.goToLogin(prevLoginEmail.value)
            }
        )

        Spacer(modifier = Modifier.padding(top = 44.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .padding(horizontal = 28.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MyTextField(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    value = prevLoginEmail.value,
                    textStyle = TextStyle(
                        color = Gray10, fontSize = dpToSp(24.dp), fontWeight = FontWeight.Medium
                    ),
                    onValueChange = {
                        prevLoginEmail.value = it
                    },
                    decorationBox = { innerTextField ->
                        Row {
                            if (prevLoginEmail.value.isEmpty()) {
                                MyText(
                                    text = "이메일을 입력하세요",
                                    color = Gray6,
                                    fontSize = dpToSp(24.dp)
                                )
                            }
                            innerTextField()  //<-- Add this
                        }
                    },
                )
            }
        }
    }
    if (isAlreadyUsedEmail.value) {
        CommonDialogView(
            title = stringResource(id = R.string.alreadyUsedEmailTitle),
            message = stringResource(id = R.string.alreadyUsedEmailDesc),
            dismissAvailable = false,
            negative = DialogButtonInfo(text = CommonR.string.closeDesc, color = Gray7),
            positive = DialogButtonInfo(text = CommonR.string.goToLogin, color = Main4),
            negativeEvent = {
                isAlreadyUsedEmail.value = false
                goToBack()
            },
            positiveEvent = {
                isAlreadyUsedEmail.value = false
                viewModel.savedLoginEmail(prevLoginEmail.value)
            }
        )
    }
    if (isFailApi.value) {
        ApiFailDialog(
            title = stringResource(id = R.string.joinFailTitle),
            message = stringResource(id = R.string.loginRetry)
        ) {
            Log.e("ayhan", "api Faaaaaa")
            isFailApi.value = false
        }
    }
}
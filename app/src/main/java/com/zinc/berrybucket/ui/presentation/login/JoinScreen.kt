package com.zinc.berrybucket.ui.presentation.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.R
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.component.MyTextField
import com.zinc.berrybucket.ui.presentation.component.TitleIconType
import com.zinc.berrybucket.ui.presentation.component.TitleView
import com.zinc.berrybucket.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.berrybucket.ui.presentation.component.profile.ProfileEditView
import com.zinc.berrybucket.ui.presentation.component.profile.ProfileUpdateView
import com.zinc.berrybucket.ui.presentation.model.ProfileEditData
import com.zinc.berrybucket.ui.util.dpToSp
import java.io.File

@Composable
fun JoinScreen(
    goToMain: () -> Unit,
    goToBack: () -> Unit
) {
    val viewModel: JoinViewModel = hiltViewModel()

    val checkAlreadyUsedNickname by viewModel.isAlreadyUsedNickName.observeAsState()
    val failJoinAsState by viewModel.failJoin.observeAsState()
    val goToLoginAsState by viewModel.goToLogin.observeAsState()
    val goToCreateLogin by viewModel.goToCreateProfile.observeAsState()

    val isDataChanged = remember { mutableStateOf(false) }
    var showSelectCameraType by remember { mutableStateOf(false) }
    val isAlreadyUsedNickname = remember { mutableStateOf(false) }

    val updateImageFile: MutableState<File?> = remember { mutableStateOf(null) }
    val updateImagePath: MutableState<String?> = remember { mutableStateOf(null) }

    val isJoinFailed = remember { mutableStateOf(failJoinAsState) }
    val tryLoginEmail = remember { mutableStateOf("") }
    val prevLoginEmail = remember { mutableStateOf("") }

    val nickNameData = remember {
        mutableStateOf(
            ProfileEditData(
                dataType = ProfileEditData.ProfileDataType.NICKNAME,
                prevText = ""
            )
        )
    }

    val bioData = remember {
        mutableStateOf(
            ProfileEditData(
                dataType = ProfileEditData.ProfileDataType.BIO,
                prevText = ""
            )
        )
    }

    LaunchedEffect(key1 = checkAlreadyUsedNickname) {
        isAlreadyUsedNickname.value = checkAlreadyUsedNickname ?: false
        if (checkAlreadyUsedNickname == false) {
            viewModel.createNewProfile(
                email = tryLoginEmail.value,
                nickName = nickNameData.value.prevText,
                bio = bioData.value.prevText,
                image = updateImageFile.value
            )
        } else {
            isDataChanged.value = false
        }
    }

    LaunchedEffect(key1 = nickNameData.value, key2 = bioData.value, block = {
        isDataChanged.value =
            nickNameData.value.prevText.isEmpty() || bioData.value.prevText.isEmpty()
    })

    LaunchedEffect(key1 = failJoinAsState) {
        if (failJoinAsState == true) {
            isJoinFailed.value = true
        }
    }

    LaunchedEffect(key1 = goToLoginAsState) {
        if (goToLoginAsState == true) {
            goToMain()
        }
    }

    if (goToCreateLogin == null && tryLoginEmail.value.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileCreateTitle(
                saveButtonEnable = isDataChanged.value,
                backClicked = {
                    goToBack()
                },
                saveClicked = {
                    tryLoginEmail.value = prevLoginEmail.value
                    viewModel.createUserToken(tryLoginEmail.value)
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
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileCreateTitle(
                saveButtonEnable = isDataChanged.value,
                backClicked = {
                    goToBack()
                },
                saveClicked = {
                    if (nickNameData.value.prevText.isNotEmpty()) {
                        viewModel.checkIsAlreadyUsedName(nickNameData.value.prevText)
                    }
                }
            )

            Spacer(modifier = Modifier.padding(top = 44.dp))

            ProfileUpdateView(
                updatePath = updateImagePath,
                imageUpdateButtonClicked = {
                    showSelectCameraType = true
                })

            Spacer(modifier = Modifier.padding(top = 41.dp))

            ProfileEditView(
                editData = nickNameData.value,
                needLengthCheck = false,
                dataChanged = { changedData ->
                    nickNameData.value = nickNameData.value.copy(prevText = changedData)
                },
                isAlreadyUsedName = isAlreadyUsedNickname.value
            )

            ProfileEditView(
                editData = bioData.value,
                needLengthCheck = true,
                dataChanged = { changedData ->
                    bioData.value = bioData.value.copy(prevText = changedData)
                },
                isAlreadyUsedName = false
            )
        }
    }


    if (isJoinFailed.value == true) {
        ApiFailDialog(
            title = stringResource(id = R.string.joinFailTitle),
            message = stringResource(id = R.string.loginRetry)
        ) {
            viewModel.checkIsAlreadyUsedName(nickNameData.value.prevText)
        }
    }
}

@Composable
internal fun ProfileCreateTitle(
    saveButtonEnable: Boolean,
    backClicked: () -> Unit,
    saveClicked: () -> Unit
) {

    val isSaveButtonEnable = remember {
        mutableStateOf(saveButtonEnable)
    }

    LaunchedEffect(key1 = saveButtonEnable, block = {
        isSaveButtonEnable.value = saveButtonEnable
    })

    TitleView(
        title = stringResource(id = R.string.goToJoin),
        leftIconType = TitleIconType.BACK,
        isDividerVisible = true,
        onLeftIconClicked = {
            backClicked()
        },
        rightText = stringResource(id = com.zinc.berrybucket.ui_common.R.string.finishDesc),
        rightTextEnable = saveButtonEnable,
        onRightTextClicked = {
            saveClicked()
        }
    )
}
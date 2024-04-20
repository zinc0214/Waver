package com.zinc.berrybucket.ui.presentation.login

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.R
import com.zinc.berrybucket.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.berrybucket.ui.presentation.component.profile.ProfileEditView
import com.zinc.berrybucket.ui.presentation.component.profile.ProfileUpdateView
import com.zinc.berrybucket.ui.presentation.login.component.ProfileCreateTitle
import com.zinc.berrybucket.ui.presentation.model.ProfileEditData
import java.io.File

@Composable
fun JoinNickNameScreen(
    email: String,
    goToMain: () -> Unit,
    goToBack: () -> Unit
) {
    val createUserViewModel: JoinNickNameViewModel = hiltViewModel()

    val checkAlreadyUsedNickname by createUserViewModel.isAlreadyUsedNickName.observeAsState()
    val failJoinAsState by createUserViewModel.failJoin.observeAsState()
    val goToLoginAsState by createUserViewModel.goToLogin.observeAsState()
    val failLoginAsState by createUserViewModel.failLogin.observeAsState()

    val isDataChanged = remember { mutableStateOf(false) }
    var showSelectCameraType by remember { mutableStateOf(false) }
    val isAlreadyUsedNickname = remember { mutableStateOf(false) }

    val updateImageFile: MutableState<File?> = remember { mutableStateOf(null) }
    val updateImagePath: MutableState<String?> = remember { mutableStateOf(null) }

    val isJoinFailed = remember { mutableStateOf(failJoinAsState) }

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
        if (checkAlreadyUsedNickname == true) {
            isDataChanged.value = false
        }
    }

    LaunchedEffect(key1 = nickNameData.value, key2 = bioData.value, block = {
        isDataChanged.value = nickNameData.value.prevText.isNotEmpty()
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

    LaunchedEffect(key1 = failLoginAsState) {
        if (failLoginAsState == true) {
            isJoinFailed.value = true
        }
    }


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
                    createUserViewModel.join(
                        email = email,
                        nickName = nickNameData.value.prevText,
                        bio = bioData.value.prevText,
                        image = updateImageFile.value
                    )
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

    if (isJoinFailed.value == true) {
        ApiFailDialog(
            title = stringResource(id = R.string.joinFailTitle),
            message = stringResource(id = R.string.loginRetry)
        ) {
            isJoinFailed.value = false
        }
    }
}
package com.zinc.waver.ui.presentation.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zinc.waver.R
import com.zinc.waver.ui.design.theme.Error2
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray4
import com.zinc.waver.ui.design.theme.Gray5
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.ImageSelectBottomScreen
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.MyTextField
import com.zinc.waver.ui.presentation.component.TitleView
import com.zinc.waver.ui.presentation.component.profile.ProfileUpdateView
import com.zinc.waver.ui.presentation.login.model.CreateProfileInfo
import com.zinc.waver.ui.presentation.model.ActionWithActivity
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui.util.isValidNicknameCheck
import com.zinc.waver.util.FileUtil.getFileFromUri
import java.io.File
import com.zinc.waver.ui_common.R as CommonR

@Composable
fun JoinCreateProfile1(
    createProfileInfo: CreateProfileInfo,
    goToNext: (CreateProfileInfo) -> Unit,
    addImageAction: (ActionWithActivity.AddImage) -> Unit,
) {
    val createUserViewModel: JoinNickNameViewModel = hiltViewModel()

    val isAlreadyUsedNickNameAsState by createUserViewModel.isAlreadyUsedNickName.observeAsState()
    val isCheckFail by createUserViewModel.failCheckNickname.observeAsState()
    var savedCreateInfo: CreateProfileInfo? by remember { mutableStateOf(null) }

    var nickNameData by remember { mutableStateOf(createProfileInfo.nickName) }
    var checkedNickName by remember { mutableStateOf("") }
    var isAlreadyUsedNickName by remember { mutableStateOf(false) }


    LaunchedEffect(isAlreadyUsedNickNameAsState) {
        isAlreadyUsedNickName = isAlreadyUsedNickNameAsState == true

        if (isAlreadyUsedNickNameAsState == false) {
            savedCreateInfo?.let {
                goToNext(it)
            }
        } else {
            savedCreateInfo = null
        }
        checkedNickName = nickNameData
    }

    LaunchedEffect(nickNameData) {
        isAlreadyUsedNickName = nickNameData != checkedNickName
    }

    JoinCreateProfile1(
        isAlreadyUsedNickName = isAlreadyUsedNickName,
        goToNext = {
            savedCreateInfo = it

            if (isAlreadyUsedNickNameAsState == false) {
                goToNext(it)
            } else {
                createUserViewModel.checkIsAlreadyUsedName(it.nickName)
            }
        },
        isCheckFail = isCheckFail ?: false,
        createProfileInfo = createProfileInfo,
        nickNameData = nickNameData,
        checkedNickName = checkedNickName,
        updateNickName = {
            nickNameData = it
        },
        addImageAction = addImageAction
    )
}

@Composable
private fun JoinCreateProfile1(
    isAlreadyUsedNickName: Boolean,
    isCheckFail: Boolean,
    createProfileInfo: CreateProfileInfo,
    nickNameData: String,
    checkedNickName: String,
    updateNickName: (String) -> Unit,
    addImageAction: (ActionWithActivity.AddImage) -> Unit,
    goToNext: (CreateProfileInfo) -> Unit,
) {
    val context = LocalContext.current

    var showSelectCameraType by remember { mutableStateOf(false) }

    val updateImageFile: MutableState<File?> =
        remember { mutableStateOf(createProfileInfo.imgFile) }
    val updateImagePath: MutableState<String?> =
        remember { mutableStateOf(createProfileInfo.imgPath) }

    val isButtonEnabled =
        (if (checkedNickName.isNotEmpty()) nickNameData != checkedNickName else true) && nickNameData.isNotEmpty() && nickNameData.length > 2 && isValidNicknameCheck(
            nickNameData
        )
    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true
    )

    LaunchedEffect(showSelectCameraType) {
        if (showSelectCameraType) {
            bottomSheetScaffoldState.show()
        } else {
            bottomSheetScaffoldState.hide()
        }
    }

    LaunchedEffect(bottomSheetScaffoldState.currentValue) {
        if (bottomSheetScaffoldState.currentValue == ModalBottomSheetValue.Hidden) {
            showSelectCameraType = false
        }
    }

    LaunchedEffect(Unit) {
        val randomProfile = listOf(
            CommonR.drawable.profile_icon_1,
            CommonR.drawable.profile_icon_2,
            CommonR.drawable.profile_icon_3
        ).random()

        try {
            val uri = ("android.resource://" + context.packageName + "/" + randomProfile).toUri();
            val file = getFileFromUri(context, uri)
            Log.e("ayhan", "randomProfile : $randomProfile , $uri , $file")
            if (file != null) {
                updateImageFile.value = file
                updateImagePath.value = file.path
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "이미지 처리 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show()
        }

    }

    ModalBottomSheetLayout(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
        sheetState = bottomSheetScaffoldState,
        sheetContent = {
            if (showSelectCameraType) {
                ImageSelectBottomScreen(selectedType = {
                    addImageAction.invoke(
                        ActionWithActivity.AddImage(
                            type = it,
                            failed = {
                                Toast.makeText(
                                    context,
                                    "이미지 로드에 실패했습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                showSelectCameraType = false
                            },
                            succeed = { imageInfo ->
                                Log.e("ayhan", "imageInfo : $imageInfo")
                                showSelectCameraType = false
                                updateImagePath.value = imageInfo.path
                                updateImageFile.value = imageInfo.file
                            })
                    )
                })
            }
        },
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TitleView(
                title = stringResource(R.string.goToJoin1),
                isDividerVisible = true
            )

            Spacer(modifier = Modifier.padding(top = 36.dp))

            ProfileUpdateView(
                currentProfileUri = null,
                updatePath = updateImagePath,
                imageUpdateButtonClicked = {
                    showSelectCameraType = true
                })

            Spacer(modifier = Modifier.padding(top = 47.dp))

            ProfileNickNameEditView(
                prevNickName = nickNameData,
                isAlreadyUsedName = isAlreadyUsedNickName && nickNameData == checkedNickName,
                isCheckFail = isCheckFail,
                dataChanged = {
                    updateNickName(it)
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            MyText(
                text = stringResource(CommonR.string.next),
                fontSize = dpToSp(16.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = Gray1,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = if (isButtonEnabled) Main4 else Gray5)
                    .clickable(isButtonEnabled) {
                        goToNext(
                            CreateProfileInfo(
                                nickName = nickNameData,
                                imgPath = updateImagePath.value,
                                imgFile = updateImageFile.value
                            )
                        )
                    }
                    .padding(vertical = 18.dp)
            )
        }
    }
}

@Composable
private fun ProfileNickNameEditView(
    prevNickName: String,
    dataChanged: (String) -> Unit,
    isAlreadyUsedName: Boolean,
    isCheckFail: Boolean
) {
    val showError = isAlreadyUsedName || isCheckFail
    var isFocused by remember { mutableStateOf(false) }
    var isValidNickname by remember { mutableStateOf(false) }

    LaunchedEffect(prevNickName) {
        isValidNickname = isValidNicknameCheck(prevNickName)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .padding(horizontal = 28.dp)
    ) {

        MyText(
            text = stringResource(id = CommonR.string.profileSettingNickNameTitle),
            color = Gray6,
            fontSize = dpToSp(dp = 14.dp)
        )

        MyTextField(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            value = prevNickName,
            singleLine = true,
            textStyle = TextStyle(fontSize = dpToSp(dp = 20.dp)),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            onValueChange = { changeText ->
                dataChanged(changeText)
            },
            decorationBox = { innerTextField ->
                Row {
                    if (prevNickName.isEmpty() && !isFocused) {
                        MyText(
                            text = stringResource(CommonR.string.addProfileNickname),
                            color = Gray4,
                            fontSize = dpToSp(20.dp)
                        )
                    }
                    innerTextField()  //<-- Add this
                }
            }
        )

        Divider(
            modifier = Modifier.padding(top = 15.5.dp),
            color = if (showError) Error2 else Gray5,
            thickness = 1.dp
        )

        if (showError) {
            val text =
                if (isCheckFail) CommonR.string.nicknameCheckFail else CommonR.string.alreadyUsedNickNameGuide
            MyText(
                modifier = Modifier.padding(top = 7.5.dp),
                text = stringResource(id = text),
                color = Error2,
                fontSize = dpToSp(dp = 12.dp)
            )
        }

        if (prevNickName.length < 3 || !isValidNickname) {
            val text = CommonR.string.enterCharacter
            MyText(
                modifier = Modifier.padding(top = 7.5.dp),
                text = stringResource(id = text),
                color = Gray6,
                fontSize = dpToSp(dp = 12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun JoinNickNameScreenPreview() {
    JoinCreateProfile1(
        nickNameData = "",
        createProfileInfo = CreateProfileInfo(),
        goToNext = { },
        isAlreadyUsedNickName = false,
        isCheckFail = false,
        checkedNickName = "",
        updateNickName = {},
        addImageAction = {})
}

@Preview(showBackground = true)
@Composable
private fun ProfileNickNameEditViewPreview() {
    ProfileNickNameEditView(
        prevNickName = "",
        dataChanged = {},
        isAlreadyUsedName = false,
        isCheckFail = false
    )
}
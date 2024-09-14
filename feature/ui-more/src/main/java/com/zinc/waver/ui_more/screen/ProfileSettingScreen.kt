package com.zinc.waver.ui_more.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.waver.model.DialogButtonInfo
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.ImageSelectBottomScreen
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.waver.ui.presentation.component.dialog.CommonDialogView
import com.zinc.waver.ui.presentation.component.profile.ProfileEditView
import com.zinc.waver.ui.presentation.component.profile.ProfileUpdateView
import com.zinc.waver.ui.presentation.model.ActionWithActivity
import com.zinc.waver.ui.presentation.model.ProfileEditData
import com.zinc.waver.ui_more.R
import com.zinc.waver.ui_more.components.ProfileSettingTitle
import com.zinc.waver.ui_more.viewModel.MoreViewModel
import kotlinx.coroutines.launch
import java.io.File
import com.zinc.waver.ui_common.R as CommonR

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileSettingScreen(
    onBackPressed: () -> Unit,
    addImageAction: (ActionWithActivity.AddImage) -> Unit,
) {

    val viewModel: MoreViewModel = hiltViewModel()

    val profileInfoAsState by viewModel.profileInfo.observeAsState()
    val loadProfileFail by viewModel.profileLoadFail.observeAsState()
    val profileUpdateSucceedAsSate by viewModel.profileUpdateSucceed.observeAsState()
    val profileUpdateFailAsState by viewModel.profileUpdateFail.observeAsState()
    val checkAlreadyUsedNickname by viewModel.isAlreadyUsedNickName.observeAsState()

    val profileInfo = remember { mutableStateOf(profileInfoAsState) }
    val isDataChanged = remember { mutableStateOf(false) }
    val showApiFailDialog = remember { mutableStateOf(false) }
    val showUpdateSucceedDialog = remember { mutableStateOf(false) }
    val showUpdateFailDialog = remember { mutableStateOf(false) }
    val isAlreadyUsedNickname = remember { mutableStateOf(false) }

    val updateImagePath: MutableState<String?> = remember { mutableStateOf(null) }
    val updateImageFile: MutableState<File?> = remember { mutableStateOf(null) }
    var showSelectCameraType by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true
    )

    if (profileInfo.value == null) {
        viewModel.loadMyProfile()
    }

    val nickNameData = remember {
        mutableStateOf(
            ProfileEditData(
                dataType = ProfileEditData.ProfileDataType.NICKNAME,
                prevText = profileInfo.value?.name.orEmpty()
            )
        )

    }
    val bioData = remember {
        mutableStateOf(
            ProfileEditData(
                dataType = ProfileEditData.ProfileDataType.BIO,
                prevText = profileInfo.value?.bio.orEmpty()
            )
        )
    }

    LaunchedEffect(key1 = loadProfileFail) {
        showApiFailDialog.value = loadProfileFail ?: false
    }

    LaunchedEffect(key1 = profileUpdateSucceedAsSate) {
        showUpdateSucceedDialog.value = profileUpdateSucceedAsSate ?: false
    }

    LaunchedEffect(key1 = profileUpdateFailAsState) {
        showUpdateFailDialog.value = profileUpdateFailAsState ?: false
    }

    LaunchedEffect(key1 = checkAlreadyUsedNickname) {
        isAlreadyUsedNickname.value = checkAlreadyUsedNickname ?: false
        if (checkAlreadyUsedNickname == false) {
            viewModel.updateMyProfile(
                name = nickNameData.value.prevText,
                bio = bioData.value.prevText,
                profileImage = updateImageFile.value
            )
        } else {
            isDataChanged.value = false
        }
    }

    LaunchedEffect(key1 = profileInfoAsState) {
        profileInfo.value = profileInfoAsState
        nickNameData.value = nickNameData.value.copy(prevText = profileInfo.value?.name.orEmpty())
        bioData.value = bioData.value.copy(prevText = profileInfo.value?.bio.orEmpty())
    }

    LaunchedEffect(
        key1 = nickNameData.value,
        key2 = bioData.value,
        key3 = updateImagePath.value,
        block = {
            val isEmpty = nickNameData.value.prevText.isEmpty() || bioData.value.prevText.isEmpty()
            isDataChanged.value = isEmpty.not() &&
                    (profileInfo.value?.name != nickNameData.value.prevText || profileInfo.value?.bio != bioData.value.prevText || updateImagePath.value != null)
        })

    val isNeedToBottomSheetOpen: (Boolean) -> Unit = {
        coroutineScope.launch {
            if (it) {
                bottomSheetScaffoldState.show()
            } else {
                bottomSheetScaffoldState.hide()
            }
        }
    }
    val context = LocalContext.current

    LaunchedEffect(bottomSheetScaffoldState.currentValue) {
        when (bottomSheetScaffoldState.currentValue) {
            ModalBottomSheetValue.Hidden -> {
                showSelectCameraType = false
                isNeedToBottomSheetOpen(false)
            }

            else -> {
                // Do Nothing
            }
        }
    }

    ModalBottomSheetLayout(
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
                                isNeedToBottomSheetOpen.invoke(false)
                            },
                            succeed = { imageInfo ->
                                Log.e("ayhan", "imageInfo : $imageInfo")

                                isNeedToBottomSheetOpen.invoke(false)
                                updateImagePath.value = imageInfo.path
                                updateImageFile.value = imageInfo.file
                            })
                    )

                })
                isNeedToBottomSheetOpen.invoke(true)
            }

        },
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
    ) {
        profileInfo.value?.let { profile ->
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileSettingTitle(
                    saveButtonEnable = isDataChanged.value,
                    backClicked = {
                        onBackPressed()
                    },
                    saveClicked = {
                        if (profile.name != nickNameData.value.prevText) {
                            viewModel.checkIsAlreadyUsedName(nickNameData.value.prevText)
                        } else {
                            viewModel.updateMyProfile(
                                name = nickNameData.value.prevText,
                                bio = bioData.value.prevText,
                                profileImage = updateImageFile.value
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
        }
    }

    if (showApiFailDialog.value) {
        ApiFailDialog(
            title = stringResource(id = R.string.loadFailProfile),
            message = stringResource(id = CommonR.string.retryDesc),
            dismissEvent = {
                showApiFailDialog.value = false
                onBackPressed()
            })
    }

    if (showUpdateSucceedDialog.value) {
        CommonDialogView(
            message = stringResource(id = R.string.profileUpdateSuccessTitle),
            dismissAvailable = false,
            positive = DialogButtonInfo(text = CommonR.string.confirm, color = Main4),
            positiveEvent = {
                showUpdateSucceedDialog.value = false
                onBackPressed()
            })
    }

    if (showUpdateFailDialog.value) {
        ApiFailDialog(
            message = stringResource(id = R.string.profileUpdateFailTitle),
            dismissEvent = {
                showApiFailDialog.value = false
            })
    }
}
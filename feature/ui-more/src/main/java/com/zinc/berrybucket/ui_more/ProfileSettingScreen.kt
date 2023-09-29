package com.zinc.berrybucket.ui_more

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
import com.zinc.berrybucket.model.AddImageType
import com.zinc.berrybucket.ui.presentation.component.ImageSelectBottomScreen
import com.zinc.berrybucket.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.berrybucket.ui_more.components.ProfileEditView
import com.zinc.berrybucket.ui_more.components.ProfileSettingTitle
import com.zinc.berrybucket.ui_more.components.ProfileUpdateView
import com.zinc.berrybucket.ui_more.models.ProfileEditData
import com.zinc.berrybucket.ui_more.viewModel.MoreViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileSettingScreen(
    onBackPressed: () -> Unit,
    imageUpdateButtonClicked: (AddImageType, () -> Unit, (String) -> Unit) -> Unit
) {

    val viewModel: MoreViewModel = hiltViewModel()

    val profileInfoAsState by viewModel.profileInfo.observeAsState()
    val loadProfileFail by viewModel.profileLoadFail.observeAsState()

    val showApiFailDialog = remember { mutableStateOf(false) }
    val profileInfo = remember { mutableStateOf(profileInfoAsState) }
    val isDataChanged = remember { mutableStateOf(false) }

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

    LaunchedEffect(key1 = profileInfoAsState) {
        profileInfo.value = profileInfoAsState
        nickNameData.value = nickNameData.value.copy(prevText = profileInfo.value?.name.orEmpty())
        bioData.value = bioData.value.copy(prevText = profileInfo.value?.bio.orEmpty())
    }

    LaunchedEffect(key1 = nickNameData.value, key2 = bioData.value, block = {
        val isEmpty = nickNameData.value.prevText.isEmpty() || bioData.value.prevText.isEmpty()
        isDataChanged.value = isEmpty.not() &&
                (profileInfo.value?.name != nickNameData.value.prevText || profileInfo.value?.bio != bioData.value.prevText)
    })

    val updatePath: MutableState<String?> = remember { mutableStateOf(null) }
    var showSelectCameraType by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true
    )
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
                    imageUpdateButtonClicked(it,
                        // fail
                        {
                            Toast.makeText(
                                context,
                                "이미지 로드에 실패했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            isNeedToBottomSheetOpen.invoke(false)
                            showSelectCameraType = false
                        },
                        // success
                        { imagePath ->
                            isNeedToBottomSheetOpen.invoke(false)
                            showSelectCameraType = false
                            updatePath.value = imagePath
                        })
                })
                isNeedToBottomSheetOpen.invoke(true)
            }

        },
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
    ) {
        profileInfo.value?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileSettingTitle(
                    saveButtonEnable = isDataChanged.value,
                    backClicked = {
                        onBackPressed()
                    }
                )

                Spacer(modifier = Modifier.padding(top = 44.dp))

                ProfileUpdateView(
                    updatePath = updatePath,
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
                    isAlreadyUsedName = false
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
            message = stringResource(id = R.string.retryDesc),
            dismissEvent = {
                showApiFailDialog.value = false
                onBackPressed()
            })
    }
}
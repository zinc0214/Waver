package com.zinc.berrybucket.ui_more

import android.net.Uri
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.model.AddImageType
import com.zinc.berrybucket.model.UserSelectedImageInfo
import com.zinc.berrybucket.ui.presentation.component.ImageSelectBottomScreen
import com.zinc.berrybucket.ui_more.components.ProfileEditView
import com.zinc.berrybucket.ui_more.components.ProfileSettingTitle
import com.zinc.berrybucket.ui_more.components.ProfileUpdateView
import com.zinc.berrybucket.ui_more.models.ProfileEditData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileSettingScreen(
    onBackPressed: () -> Unit,
    imageUpdateButtonClicked: (AddImageType, () -> Unit, (UserSelectedImageInfo) -> Unit) -> Unit
) {

    val updateUri: MutableState<Uri?> = remember { mutableStateOf(null) }
    var showSelectCameraType by remember { mutableStateOf(false) }
    val nickNameData = remember {
        ProfileEditData(
            title = "닉네임",
            prevText = "맹꽁이"
        )
    }
    val bioData = remember {
        ProfileEditData(
            title = "소개문구",
            prevText = "맹꽁이라고 하거든, 반가웡"
        )
    }

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
                        { imageInfo ->
                            isNeedToBottomSheetOpen.invoke(false)
                            showSelectCameraType = false
                            updateUri.value = imageInfo.uri
                        })
                })
                isNeedToBottomSheetOpen.invoke(true)
            }

        },
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileSettingTitle {
                onBackPressed()
            }

            Spacer(modifier = Modifier.padding(top = 44.dp))

            ProfileUpdateView(
                updateUri = updateUri,
                imageUpdateButtonClicked = {
                    showSelectCameraType = true
                })

            Spacer(modifier = Modifier.padding(top = 41.dp))

            ProfileEditView(
                editData = nickNameData,
                needLengthCheck = false,
                dataChanged = { changedData ->
                    nickNameData.copy(prevText = changedData)
                })

            ProfileEditView(
                editData = bioData,
                needLengthCheck = true,
                dataChanged = { changedData ->
                    bioData.copy(prevText = changedData)
                })
        }
    }
}
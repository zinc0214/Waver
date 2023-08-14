package com.zinc.berrybucket.ui_more.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray2
import com.zinc.berrybucket.ui.design.theme.Gray5
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.presentation.component.IconButton
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.component.MyTextField
import com.zinc.berrybucket.ui.presentation.component.TitleIconType
import com.zinc.berrybucket.ui.presentation.component.TitleView
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_more.R
import com.zinc.berrybucket.ui_more.models.ProfileEditData
import com.zinc.berrybucket.util.loadImage
import com.zinc.berrybucket.ui.util.CameraPermission as CameraPermission1

@Composable
internal fun ProfileSettingTitle(backClicked: () -> Unit) {
    TitleView(
        title = stringResource(id = R.string.profileSettingTitle),
        leftIconType = TitleIconType.BACK,
        isDividerVisible = true,
        onLeftIconClicked = {
            backClicked()
        },
        rightText = stringResource(id = com.zinc.berrybucket.ui_common.R.string.finishDesc),
        onRightTextClicked = {
            // TODO : 알림 데이터 저장 필요
        }
    )
}

@Composable
internal fun ProfileUpdateView(
    updatePath: MutableState<String?>,
    imageUpdateButtonClicked: () -> Unit
) {

    val profileUri =
        if (updatePath.value != null) loadImage(path = updatePath.value!!, index = 0).uri else null
    val showPermission = remember { mutableStateOf(false) }
    val hasPermission = remember { mutableStateOf(false) }

    if (showPermission.value) {
        CheckCameraPermission {
            hasPermission.value = it
            showPermission.value = false
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .width(76.dp)
            .wrapContentHeight()
    ) {

        val (profileImg, updateButton) = createRefs()

        Image(
            painter = rememberAsyncImagePainter(
                model = profileUri,
                error = painterResource(id = com.zinc.berrybucket.ui_common.R.drawable.test)
            ),
            contentDescription = stringResource(
                id = R.string.moreProfileImageDesc
            ),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(profileImg) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
                .size(70.dp, 70.dp)
                .aspectRatio(1f)
                .clip(shape = RoundedCornerShape(28.dp))
                .border(width = 1.dp, color = Gray2, shape = RoundedCornerShape(28.dp))
        )

        Box(
            modifier = Modifier
                .constrainAs(updateButton) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .background(color = Gray10, shape = CircleShape)
                .border(width = 1.dp, shape = CircleShape, color = Gray1)
                .size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                modifier = Modifier.size(20.dp),
                onClick = {
                    if (hasPermission.value) {
                        showPermission.value = true
                    } else {
                        imageUpdateButtonClicked()
                    }
                },
                image = R.drawable.ico_20_camera,
                contentDescription = stringResource(id = R.string.profileChangeDesc)
            )
        }

    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun CheckCameraPermission(isAvailable: (Boolean) -> Unit) {
    CameraPermission1 {
        isAvailable(it)
    }
}

@Composable
internal fun ProfileEditView(
    editData: ProfileEditData,
    needLengthCheck: Boolean,
    dataChanged: (String) -> Unit
) {

    var currentText by remember { mutableStateOf(editData.prevText) }
    var currentTextSize by remember { mutableStateOf(currentText.length) }
    val maxLength = 30
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .padding(horizontal = 28.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

            MyText(
                text = editData.title,
                color = Gray6,
                fontSize = dpToSp(dp = 14.dp)
            )

            if (needLengthCheck) {
                Spacer(modifier = Modifier.weight(1f))
                MyText(
                    text = "$currentTextSize/$maxLength",
                    color = Gray5,
                    fontSize = dpToSp(dp = 12.dp)
                )
            }
        }

        MyTextField(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            value = currentText,
            onValueChange = { changeText ->
                currentText = if (changeText.length > 30) {
                    val lastIndex = changeText.lastIndex
                    changeText.removeRange(maxLength - 1, lastIndex)
                } else {
                    changeText
                }
                currentTextSize = currentText.length
                dataChanged(changeText)
            })
        Divider(color = Gray5, thickness = 1.dp)
    }
}
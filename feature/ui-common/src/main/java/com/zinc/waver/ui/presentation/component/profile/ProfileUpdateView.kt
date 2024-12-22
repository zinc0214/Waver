package com.zinc.waver.ui.presentation.component.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.presentation.component.IconButton
import com.zinc.waver.ui.util.CameraPermission
import com.zinc.waver.ui_common.R
import com.zinc.waver.util.createImageInfoWithPath

@Composable
fun ProfileUpdateView(
    updatePath: MutableState<String?>,
    imageUpdateButtonClicked: () -> Unit
) {

    val context = LocalContext.current
    val profileUri =
        if (updatePath.value != null) createImageInfoWithPath(
            context = context,
            path = updatePath.value!!,
            index = 0
        ).uri else null
    var showPermission by remember { mutableStateOf(false) }
    var hasPermission by remember { mutableStateOf(false) }

    Log.e("ayhan", "showPermission1 : ${showPermission} , ${hasPermission}")

    if (!hasPermission || showPermission) {
        Log.e("ayhan", "showPermission")
        CheckCameraPermission {
            hasPermission = it
            showPermission = false

            Log.e("ayhan", "CheckCameraPermission : $it")
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
                placeholder = painterResource(id = R.drawable.profile_icon_1),
                error = painterResource(id = R.drawable.profile_icon_1)
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
                    Log.e("ayhan", "click! ${hasPermission}")
                    if (hasPermission) {
                        imageUpdateButtonClicked()
                    } else {
                        showPermission = true
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
    CameraPermission {
        isAvailable(it)
    }
}

@Preview
@Composable
private fun ProfileUpdateViewPreview() {
    val updateImageFile: MutableState<String?> = remember { mutableStateOf("") }

    ProfileUpdateView(updatePath = updateImageFile, imageUpdateButtonClicked = {})
}
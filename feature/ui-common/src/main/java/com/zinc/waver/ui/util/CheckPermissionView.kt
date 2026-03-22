package com.zinc.waver.ui.util

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui_common.R


@ExperimentalPermissionsApi
@Composable
fun CameraPermission(
    isAvailable: (Boolean) -> Unit
) {
    CheckPermissionView(isAvailable = {
        isAvailable(it)
    })
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckPermissionView(
    isAvailable: (Boolean) -> Unit
) {

    val context = LocalContext.current

    val permissionStates = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // Android 13 이상: READ_MEDIA_IMAGES 사용
        rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        )
    } else {
        // Android 12 이하: READ_EXTERNAL_STORAGE 사용
        rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    permissionStates.launchMultiplePermissionRequest()
                }

                else -> {
                    // Do Nothing
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    // allPermissionsGranted 또는 하나 이상의 권한이 부여된 경우 true
    val hasAnyPermission = permissionStates.permissions.any {
        it.status is PermissionStatus.Granted
    }

    if (hasAnyPermission) {
        isAvailable(true)
    }

    permissionStates.permissions.forEach {
        when (it.permission) {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_EXTERNAL_STORAGE -> {
                when (it.status) {
                    is PermissionStatus.Granted -> {
                        // 권한 허용됨
                    }

                    is PermissionStatus.Denied -> {
                        // 권한 거부됨 - 재요청 팝업 표시
                        if ((it.status as PermissionStatus.Denied).shouldShowRationale) {
                            RationaleDialog(
                                titleResId = R.string.permissionRequestTitle,
                                messageResId = R.string.permissionRequestMessage,
                                buttonResId = R.string.permissionOkButton,
                                onRequestPermission = {
                                    val intent = Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.parse("package:" + context.packageName)
                                    )
                                    context.startActivity(intent)
                                    isAvailable(false)
                                })
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun RationaleDialog(
    titleResId: Int,
    messageResId: Int,
    buttonResId: Int,
    onRequestPermission: () -> Unit
) {
    val title = stringResource(id = titleResId)
    val message = stringResource(id = messageResId)
    val buttonText = stringResource(id = buttonResId)

    AlertDialog(onDismissRequest = { },
        title = {
            MyText(text = title)
        }, text = {
            MyText(message)
        }, dismissButton = {
            Button(onClick = {
                onRequestPermission()
            }) {
                MyText(buttonText)
            }
        }, confirmButton = {

        })
}
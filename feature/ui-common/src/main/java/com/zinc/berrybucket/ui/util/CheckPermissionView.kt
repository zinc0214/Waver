package com.zinc.berrybucket.ui.util

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.zinc.berrybucket.ui.presentation.common.MyText


@ExperimentalPermissionsApi
@Composable
fun CameraPermission(
    isAvailable: (Boolean) -> Unit
) {
    val context = LocalContext.current
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

    val permissionStates = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_MEDIA_LOCATION
        )
    )

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

    if (permissionStates.allPermissionsGranted) {
        isAvailable(true)
    }

    permissionStates.permissions.forEach {
        when (it.permission) {
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_MEDIA_LOCATION -> {
                when (it.status) {
                    is PermissionStatus.Granted -> {

                    }

                    is PermissionStatus.Denied -> {
                        RationaleDialog(text = "카메라/갤러리 접근을 위해\n권한이 필요합니다",
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

@Composable
private fun RationaleDialog(
    text: String, onRequestPermission: () -> Unit
) {
    AlertDialog(onDismissRequest = { },
        title = {
            MyText(text = "Permission request")
        }, text = {
            MyText(text)
        }, dismissButton = {
            Button(onClick = {
                onRequestPermission()

            }) {
                MyText("Ok")
            }
        }, confirmButton = {

        })
}
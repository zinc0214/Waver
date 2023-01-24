package com.zinc.berrybucket.ui.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier, isAvailable: (Boolean) -> Unit
) {
    val context = LocalContext.current
    Permission(permissionsNotAvailableContent = {
        Button(onClick = {
            context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            })
        }) {
            MyText("Open Settings")
        }
    }, content = {
        isAvailable(true)
    })
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun Permission(
    permissionsNotAvailableContent: @Composable () -> Unit = { },
    content: @Composable () -> Unit = { }
) {
    val permissionStates = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_MEDIA_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        permissionStates.permissions.forEach { it ->
            when (it.permission) {
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_MEDIA_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                    when (it.status) {
                        is PermissionStatus.Granted -> {

                        }
                        is PermissionStatus.Denied -> {
                            RationaleDialog(text = "카메라/갤러리 접근을 위해\n권한이 필요합니다",
                                onRequestPermission = { permissionStates.launchMultiplePermissionRequest() })
                        }
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
    AlertDialog(onDismissRequest = { /* Don't */ }, title = {
        MyText(text = "Permission request")
    }, text = {
        MyText(text)
    }, confirmButton = {
        Button(onClick = onRequestPermission) {
            MyText("Ok")
        }
    })
}
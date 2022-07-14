package com.zinc.berrybucket.ui.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@ExperimentalPermissionsApi
@Composable
fun CameraPermission(
    modifier: Modifier = Modifier, isAvailable: (Boolean) -> Unit
) {
    val context = LocalContext.current
    Permission(
        permissionsNotAvailableContent = {
            Button(onClick = {
                context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                })
            }) {
                Text("Open Settings")
            }
        },
        content = {
            isAvailable(true)
        })
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun Permission(
    permissionsNotAvailableContent: @Composable () -> Unit = { },
    content: @Composable () -> Unit = { }
) {
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_MEDIA_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )
    PermissionsRequired(
        multiplePermissionsState = permissionState,
        permissionsNotGrantedContent = {
            Rationale(text = "카메라/갤러리 접근을 위해\n권한이 필요합니다",
                onRequestPermission = { permissionState.launchMultiplePermissionRequest() })
        },
        permissionsNotAvailableContent = permissionsNotAvailableContent,
        content = content
    )
}

@Composable
private fun Rationale(
    text: String, onRequestPermission: () -> Unit
) {
    AlertDialog(onDismissRequest = { /* Don't */ }, title = {
        Text(text = "Permission request")
    }, text = {
        Text(text)
    }, confirmButton = {
        Button(onClick = onRequestPermission) {
            Text("Ok")
        }
    })
}
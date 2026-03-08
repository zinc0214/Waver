package com.zinc.waver.ui_more.components

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.Switch
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_more.R

@Composable
internal fun PushNotificationPermissionItem() {
    val context = LocalContext.current
    val activity = context as? Activity

    var isPermissionGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true // Android 12 이하에서는 항상 true
            }
        )
    }

    // Activity의 RequestPermissionCallback을 등록
    DisposableEffect(activity) {
        val callback = object : RequestPermissionCallback {
            override fun onPermissionResult(granted: Boolean) {
                isPermissionGranted = granted
            }
        }

        if (activity is RequestPermissionCallbackProvider) {
            activity.registerPermissionCallback(PERMISSION_REQUEST_CODE, callback)
        }

        onDispose {
            if (activity is RequestPermissionCallbackProvider) {
                activity.unregisterPermissionCallback(PERMISSION_REQUEST_CODE)
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (text, switch) = createRefs()

            Column(
                modifier = Modifier
                    .constrainAs(text) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(switch.start)
                        width = Dimension.fillToConstraints
                    }
                    .padding(
                        start = 28.dp,
                        top = 18.dp,
                        end = 22.dp,
                        bottom = 18.dp
                    ),
            ) {
                MyText(
                    text = stringResource(id = R.string.pushNotificationPermission),
                    color = Gray10,
                    fontSize = dpToSp(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Switch(
                modifier = Modifier
                    .constrainAs(switch) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(vertical = 12.5.dp)
                    .padding(end = 20.dp),
                isSwitchOn = isPermissionGranted,
                switchChanged = { isChecked ->
                    if (isChecked) {
                        // 권한 요청 팝업 표시
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (activity != null) {
                                // shouldShowRequestPermissionRationale을 사용하여 팝업 가능 여부 확인
                                val shouldShowRationale =
                                    ActivityCompat.shouldShowRequestPermissionRationale(
                                        activity,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    )

                                if (shouldShowRationale) {
                                    // 팝업을 다시 보여줄 수 있는 경우: 권한 요청 팝업 표시
                                    ActivityCompat.requestPermissions(
                                        activity,
                                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                        PERMISSION_REQUEST_CODE
                                    )
                                } else {
                                    // 팝업을 보여줄 수 없는 경우: 앱 설정 화면으로 이동
                                    openAppSettings(context)
                                }
                            }
                        } else {
                            isPermissionGranted = true
                        }
                    } else {
                        // 권한 비활성화를 위해 앱 설정으로 이동
                        isPermissionGranted = false
                        openAppSettings(context)
                    }
                }
            )
        }

        Divider(color = Gray3)
    }
}

/**
 * 앱 설정 화면으로 이동
 */
private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

// 권한 요청 콜백 인터페이스
interface RequestPermissionCallback {
    fun onPermissionResult(granted: Boolean)
}

// Activity에 구현할 인터페이스
interface RequestPermissionCallbackProvider {
    fun registerPermissionCallback(requestCode: Int, callback: RequestPermissionCallback)
    fun unregisterPermissionCallback(requestCode: Int)
    fun notifyPermissionResult(requestCode: Int, granted: Boolean)
}

private const val PERMISSION_REQUEST_CODE = 100

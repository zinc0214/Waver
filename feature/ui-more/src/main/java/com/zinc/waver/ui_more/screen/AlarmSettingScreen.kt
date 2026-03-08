package com.zinc.waver.ui_more.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui_more.R
import com.zinc.waver.ui_more.components.AlarmGroupItemView
import com.zinc.waver.ui_more.components.AlarmGroupTitleView
import com.zinc.waver.ui_more.components.AlarmTitle
import com.zinc.waver.ui_more.components.PushNotificationPermissionItem
import com.zinc.waver.ui_more.models.AlarmSettingType
import com.zinc.waver.ui_more.models.AlarmSwitchState

@Composable
fun AlarmSettingScreen(onBackPressed: () -> Unit) {

    val alarmSwitchStateList = remember {
        mutableStateOf(buildList {
            add(
                AlarmSwitchState(
                    type = AlarmSettingType.AlarmSettingServiceType.FOLLOWER, isOn = true
                )
            )
            add(
                AlarmSwitchState(
                    type = AlarmSettingType.AlarmSettingServiceType.LIKE, isOn = false
                )
            )
            add(
                AlarmSwitchState(
                    type = AlarmSettingType.AlarmSettingServiceType.D_DAY, isOn = true
                )
            )
            add(
                AlarmSwitchState(
                    type = AlarmSettingType.AlarmSettingServiceType.COMMENT, isOn = false
                )
            )
            add(
                AlarmSwitchState(
                    type = AlarmSettingType.AlarmSettingServiceType.FRIENDS, isOn = true
                )
            )
            add(
                AlarmSwitchState(
                    type = AlarmSettingType.AlarmSettingBenefitType.EVENT, isOn = false
                )
            )
            add(
                AlarmSwitchState(
                    type = AlarmSettingType.AlarmSettingBenefitType.UPDATE, isOn = true
                )
            )
        })
    }

    Column(modifier = Modifier
        .background(color = Gray1)
        .fillMaxSize()
        .statusBarsPadding()) {
        AlarmTitle {
            onBackPressed()
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            // 푸시 알림 권한 아이템 추가
            item {
                PushNotificationPermissionItem()
            }

            item {
                AlarmGroupTitleView(title = stringResource(id = R.string.alarmSettingServiceAlarm))
            }

            items(items = AlarmSettingType.AlarmSettingServiceType.entries, key = { type ->
                type
            }, itemContent = { alarmType ->
                AlarmGroupItemView(
                    alarmSettingType = alarmType,
                    alarmSwitchState = alarmSwitchStateList.value.first { it.type == alarmType },
                    switchChanged = { type, state ->
                        val newList = alarmSwitchStateList.value.toMutableList()
                        val index = newList.indexOfFirst { it.type == type }
                        if (index >= 0) {
                            newList[index] = newList[index].copy(isOn = state)
                            alarmSwitchStateList.value = newList
                        }
                    }
                )
            })

            item {
                AlarmGroupTitleView(title = stringResource(id = R.string.alarmSettingBenefitAlarm))
            }

            items(items = AlarmSettingType.AlarmSettingBenefitType.entries, key = { type ->
                type
            }, itemContent = { alarmType ->
                AlarmGroupItemView(
                    alarmSettingType = alarmType,
                    alarmSwitchState = alarmSwitchStateList.value.first { it.type == alarmType },
                    switchChanged = { type, state ->
                        val newList = alarmSwitchStateList.value.toMutableList()
                        val index = newList.indexOfFirst { it.type == type }
                        if (index >= 0) {
                            newList[index] = newList[index].copy(isOn = state)
                            alarmSwitchStateList.value = newList
                        }
                    }
                )
            })
        }
    }
}

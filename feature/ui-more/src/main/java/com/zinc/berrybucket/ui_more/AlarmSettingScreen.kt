package com.zinc.berrybucket.ui_more

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zinc.berrybucket.ui_more.components.AlarmGroupItemView
import com.zinc.berrybucket.ui_more.components.AlarmGroupTitleView
import com.zinc.berrybucket.ui_more.components.AlarmTitle
import com.zinc.berrybucket.ui_more.models.AlarmSettingType
import com.zinc.berrybucket.ui_more.models.AlarmSwitchState

@Composable
fun AlarmSettingScreen(onBackPressed: () -> Unit) {

    val alarmSwitchStateList = buildList {
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
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AlarmTitle {
            onBackPressed()
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                AlarmGroupTitleView(title = stringResource(id = R.string.alarmSettingServiceAlarm))
            }

            items(items = AlarmSettingType.AlarmSettingServiceType.values(), key = { type ->
                type
            }, itemContent = { alarmType ->
                AlarmGroupItemView(
                    alarmSettingType = alarmType,
                    alarmSwitchState = alarmSwitchStateList.first { it.type == alarmType },
                    switchChanged = { type, state ->
                        alarmSwitchStateList.first { it.type == type }.isOn = state
                    }
                )
            })

            item {
                AlarmGroupTitleView(title = stringResource(id = R.string.alarmSettingBenefitAlarm))
            }

            items(items = AlarmSettingType.AlarmSettingBenefitType.values(), key = { type ->
                type
            }, itemContent = { alarmType ->
                AlarmGroupItemView(
                    alarmSettingType = alarmType,
                    alarmSwitchState = alarmSwitchStateList.first { it.type == alarmType },
                    switchChanged = { type, state ->
                        alarmSwitchStateList.first { it.type == type }.isOn = state
                    }
                )
            })
        }
    }
}


package com.zinc.berrybucket.ui_more.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray3
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.presentation.common.MyText
import com.zinc.berrybucket.ui.presentation.common.Switch
import com.zinc.berrybucket.ui.presentation.common.TitleIconType
import com.zinc.berrybucket.ui.presentation.common.TitleView
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_more.R
import com.zinc.berrybucket.ui_more.models.AlarmSettingType
import com.zinc.berrybucket.ui_more.models.AlarmSwitchState

@Composable
internal fun AlarmTitle(backClicked: () -> Unit) {
    TitleView(
        title = stringResource(id = R.string.alarmSettingTitle),
        leftIconType = TitleIconType.BACK,
        isDividerVisible = true,
        onLeftIconClicked = {
            backClicked()
        },
        rightText = stringResource(id = R.string.finishDesc),
        onRightTextClicked = {
            // TODO : 알림 데이터 저장 필요
        }
    )
}

@Composable
internal fun AlarmGroupTitleView(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        MyText(
            text = title, color = Gray6, fontSize = dpToSp(dp = 14.dp),
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 50.dp)
                .padding(horizontal = 28.dp)
                .padding(top = 24.dp, bottom = 11.dp)

        )

        Divider(color = Gray3)
    }

}

@Composable
internal fun AlarmGroupItemView(
    alarmSettingType: AlarmSettingType,
    alarmSwitchState: AlarmSwitchState,
    switchChanged: (AlarmSettingType, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {

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
                    text = stringResource(id = alarmSettingType.title()),
                    color = Gray10,
                    fontSize = dpToSp(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                if (alarmSettingType.desc() != null) {
                    MyText(
                        text = stringResource(id = alarmSettingType.desc()!!),
                        color = Gray6,
                        fontSize = dpToSp(14.dp),
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth()
                    )
                }
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
                isSwitchOn = alarmSwitchState.isOn,
                switchChanged = {
                    switchChanged(alarmSettingType, it)
                }
            )
        }

        Divider(color = Gray3)
    }
}
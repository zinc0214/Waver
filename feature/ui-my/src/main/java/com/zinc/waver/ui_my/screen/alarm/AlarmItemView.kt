package com.zinc.waver.ui_my.screen.alarm

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.zinc.common.models.AlarmItem
import com.zinc.common.models.AlarmType
import com.zinc.common.models.AlarmType.COMMENT
import com.zinc.common.models.AlarmType.DDAY
import com.zinc.common.models.AlarmType.EVENT
import com.zinc.common.models.AlarmType.FRIEND
import com.zinc.common.models.AlarmType.INFO
import com.zinc.common.models.AlarmType.LIKE
import com.zinc.common.models.AlarmType.TOGETHER
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.util.HtmlText
import com.zinc.waver.ui_my.R

@Composable
fun AlarmItemView(alarmItem: AlarmItem) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val alarmIcon = getAlarmIcon(alarmItem.type)

        Image(
            modifier = Modifier
                .padding(0.dp)
                .sizeIn(36.dp),
            painter = if (alarmIcon != null) painterResource(alarmIcon)
            else rememberAsyncImagePainter(model = alarmItem.memberImg),
            contentDescription = stringResource(R.string.alarmIconDesc)
        )

        Spacer(modifier = Modifier.width(14.dp))

        HtmlText(
            html = alarmItem.title,
            fontSize = 14.dp,
            textColor = Gray9.hashCode()
        )
    }
}

private fun getAlarmIcon(type: AlarmType) = when (type) {
    LIKE -> R.drawable.btn_32_like_on
    COMMENT -> R.drawable.btn_32_coment_alarm
    DDAY -> R.drawable.btn_32_alarm_d_day
    INFO -> R.drawable.btn_32_app_noti
    EVENT -> R.drawable.btn_32_event
    TOGETHER -> com.zinc.waver.ui_common.R.drawable.ico_36_together
    FRIEND -> null
}

@Preview
@Composable
private fun AlarmItemPreview() {
    AlarmItemView(
        alarmItem = AlarmItem(
            type = COMMENT,
            title = "맹꽁이 좋아요 님이 둥가둥가",
            memberImg = null,
            bucketId = null,
            memberId = null,
        )
    )
}
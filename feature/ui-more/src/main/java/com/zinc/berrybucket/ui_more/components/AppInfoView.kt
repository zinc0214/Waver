package com.zinc.berrybucket.ui_more.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray2
import com.zinc.berrybucket.ui.design.theme.Gray4
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.design.theme.Gray7
import com.zinc.berrybucket.ui.design.theme.Main2
import com.zinc.berrybucket.ui.design.theme.Main3
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.common.MyText
import com.zinc.berrybucket.ui.presentation.common.TitleIconType
import com.zinc.berrybucket.ui.presentation.common.TitleView
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_more.R
import com.zinc.berrybucket.ui_more.models.AppInfoItemData
import com.zinc.berrybucket.ui_more.models.AppInfoItemType


@Composable
internal fun AppInfoTitle(backClicked: () -> Unit) {
    TitleView(title = stringResource(id = R.string.appInfoTitle),
        leftIconType = TitleIconType.BACK,
        isDividerVisible = true,
        onLeftIconClicked = {
            backClicked()
        })
}

@Composable
internal fun AppVersionInfo(
    appVersion: String,
    isLately: Boolean,
    gotoUpdate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Gray2)
            .padding(horizontal = 28.dp)
            .padding(top = 32.dp, bottom = 24.dp)
    ) {

        AppLogoAndVersionView(appVersion)

        AppUpdateButton(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            isLately = isLately,
            gotoUpdate = gotoUpdate
        )
    }
}

@Composable
private fun AppLogoAndVersionView(
    appVersion: String
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Spacer(
            modifier = Modifier
                .size(48.dp)
                .background(color = Main4, shape = RoundedCornerShape(12.dp))
        )

        Column(modifier = Modifier.padding(start = 12.dp)) {
            MyText(
                text = stringResource(id = R.string.appName),
                color = Gray10,
                fontSize = dpToSp(dp = 16.dp)
            )
            MyText(
                text = appVersion,
                color = Gray7,
                fontSize = dpToSp(dp = 12.dp),
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
private fun AppUpdateButton(modifier: Modifier, isLately: Boolean, gotoUpdate: () -> Unit) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = { if (isLately.not()) gotoUpdate() },
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, if (isLately) Gray4 else Main2),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Gray1,
            contentColor = if (isLately) Gray6 else Main3
        ),
        contentPadding = PaddingValues(vertical = 11.dp),
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
    ) {
        MyText(
            text = stringResource(id = if (isLately) R.string.alreadyUsedLatelyApp else R.string.needToUpdateApp),
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = dpToSp(dp = 14.dp)
        )
    }
}

@Composable
internal fun AppInfoMoreItemsView(itemClicked: (AppInfoItemType) -> Unit) {
    val itemList = buildList {
        add(AppInfoItemData(text = "이용약관", type = AppInfoItemType.USE_TERMS))
        add(AppInfoItemData(text = "개인 정보 처리 방침", type = AppInfoItemType.PERSONAL_TERMS))
        add(AppInfoItemData(text = "오츤 소스 라이브러리", type = AppInfoItemType.OPEN_SOURCE))
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        itemList.forEach {
            AppInfoMoreItemView(it) { type ->
                itemClicked(type)
            }
        }
    }
}

@Composable
private fun AppInfoMoreItemView(
    itemData: AppInfoItemData,
    itemClicked: (AppInfoItemType) -> Unit
) {

    MoreItemView(
        itemTitle = itemData.text,
        itemClicked = {
            itemClicked(itemData.type)
        })
}

@Preview
@Composable
private fun AppLogoAndVersionPreview() {
    Column {
        AppVersionInfo("1.0.0", true) {}
        AppVersionInfo("1.0.0", false) {}
    }

}
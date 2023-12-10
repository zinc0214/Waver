package com.zinc.berrybucket.ui_more.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.zinc.berrybucket.ui.presentation.component.TitleIconType
import com.zinc.berrybucket.ui.presentation.component.TitleView
import com.zinc.berrybucket.ui_more.R

@Composable
internal fun ProfileSettingTitle(
    saveButtonEnable: Boolean,
    backClicked: () -> Unit,
    saveClicked: () -> Unit
) {

    val isSaveButtonEnable = remember {
        mutableStateOf(saveButtonEnable)
    }

    LaunchedEffect(key1 = saveButtonEnable, block = {
        isSaveButtonEnable.value = saveButtonEnable
    })

    TitleView(
        title = stringResource(id = R.string.profileSettingTitle),
        leftIconType = TitleIconType.BACK,
        isDividerVisible = true,
        onLeftIconClicked = {
            backClicked()
        },
        rightText = stringResource(id = com.zinc.berrybucket.ui_common.R.string.finishDesc),
        rightTextEnable = saveButtonEnable,
        onRightTextClicked = {
            saveClicked()
        }
    )
}


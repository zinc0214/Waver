package com.zinc.waver.ui.presentation.login.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.zinc.waver.R
import com.zinc.waver.ui.presentation.component.TitleIconType
import com.zinc.waver.ui.presentation.component.TitleView

@Composable
internal fun ProfileCreateTitle(
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
        title = stringResource(id = R.string.goToJoin),
        leftIconType = TitleIconType.BACK,
        isDividerVisible = true,
        onLeftIconClicked = {
            backClicked()
        },
        rightText = stringResource(id = com.zinc.waver.ui_common.R.string.finishDesc),
        rightTextEnable = saveButtonEnable,
        onRightTextClicked = {
            saveClicked()
        }
    )
}
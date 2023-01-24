package com.zinc.berrybucket.ui_my.dday

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.model.BottomButtonClickEvent
import com.zinc.berrybucket.ui.design.theme.Gray2
import com.zinc.berrybucket.ui.design.theme.Gray7
import com.zinc.berrybucket.ui.presentation.common.BottomButtonView
import com.zinc.berrybucket.ui.presentation.common.LabelWithSwitchView
import com.zinc.berrybucket.ui.presentation.common.MyText
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_my.R
import com.zinc.berrybucket.ui_my.viewModel.MyViewModel

@Composable
fun MyDdayBucketFilterBottomScreen(
    viewModel: MyViewModel, clickEvent: (BottomButtonClickEvent) -> Unit
) {

    val proceedingBucketListSelectedState = remember {
        mutableStateOf(false)
    }

    val succeedBucketListSelectedState = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.background(
            shape = RoundedCornerShape(
                topStart = 13.dp, topEnd = 13.dp
            ), color = Gray2
        )
    ) {
        FilterTitleLabel(
            labelText = R.string.showBucketList,
            modifier = Modifier.padding(top = 32.dp, bottom = 15.dp)
        )
        LabelWithSwitchView(modifier = Modifier.padding(bottom = 10.dp),
            textLabel = R.string.dDayPreBucketList,
            isChecked = proceedingBucketListSelectedState.value,
            checkedChanged = {
                proceedingBucketListSelectedState.value = it
            })
        LabelWithSwitchView(textLabel = R.string.dDayGoneBucketList,
            isChecked = succeedBucketListSelectedState.value,
            checkedChanged = {
                succeedBucketListSelectedState.value = it
            })
        Spacer(modifier = Modifier.height(40.dp))
        BottomButtonView(clickEvent = {
            when (it) {
                BottomButtonClickEvent.LeftButtonClicked -> clickEvent.invoke(BottomButtonClickEvent.LeftButtonClicked)
                BottomButtonClickEvent.RightButtonClicked -> clickEvent.invoke(
                    BottomButtonClickEvent.RightButtonClicked
                )
            }
        })
    }
}

@Composable
private fun FilterTitleLabel(
    @StringRes labelText: Int, modifier: Modifier = Modifier
) {
    MyText(
        modifier = modifier.padding(horizontal = 28.dp),
        text = stringResource(id = labelText),
        color = Gray7,
        fontSize = dpToSp(13.dp)
    )
}
package com.zinc.berrybucket.ui.presentation.detail.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.presentation.common.MyText
import com.zinc.berrybucket.ui.presentation.detail.screen.MyBucketMenuEvent
import com.zinc.berrybucket.ui.util.dpToSp

@Composable
fun MyDetailAppBarMoreMenuDialog(
    optionPopUpShowed: MutableState<Boolean>,
    event: (MyBucketMenuEvent) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
            .padding(top = 8.dp, bottom = 8.dp),
        shape = RoundedCornerShape(40.dp),
        backgroundColor = Gray1,
        elevation = 3.dp
    ) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = { optionPopUpShowed.value = false },
            offset = DpOffset(16.dp, 0.dp),
            properties = PopupProperties(clippingEnabled = false)
        ) {
            DropdownMenuItem(
                onClick = {
                    event(MyBucketMenuEvent.GoToEdit)
                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .sizeIn(
                        maxHeight = 36.dp
                    )
            ) {
                PppUpText(com.zinc.berrybucket.ui_common.R.string.edit)
            }
            DropdownMenuItem(
                onClick = {
                    event(MyBucketMenuEvent.GoToGoalUpdate)
                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .sizeIn(
                        maxHeight = 36.dp
                    )
            ) {
                PppUpText(com.zinc.berrybucket.ui_common.R.string.countChange)
            }
            DropdownMenuItem(
                onClick = {
                    event(MyBucketMenuEvent.GoToDelete)
                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .sizeIn(
                        maxHeight = 36.dp
                    )
            ) {
                PppUpText(com.zinc.berrybucket.ui_common.R.string.delete)
            }
        }
    }
}

@Composable
private fun PppUpText(@StringRes text: Int) {
    MyText(
        text = stringResource(id = text),
        color = Gray10,
        fontSize = dpToSp(14.dp),
        modifier = Modifier.padding(start = 16.dp, end = 24.dp)
    )
}
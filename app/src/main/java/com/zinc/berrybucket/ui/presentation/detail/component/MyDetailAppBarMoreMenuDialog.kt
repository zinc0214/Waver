package com.zinc.berrybucket.ui.presentation.detail.component

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
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
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.detail.screen.MyBucketMenuEvent
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_common.R

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
        elevation = 2.dp
    ) {
        MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(8.dp))) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = { optionPopUpShowed.value = false },
                offset = DpOffset(16.dp, 0.dp),
                properties = PopupProperties(clippingEnabled = false)
            ) {
                PppUpText(R.string.edit) {
                    event(MyBucketMenuEvent.GoToEdit)
                }

                PppUpText(R.string.countChange) {
                    event(MyBucketMenuEvent.GoToGoalUpdate)
                }

                PppUpText(R.string.delete) {
                    event(MyBucketMenuEvent.GoToDelete)
                }
            }
        }
    }
}

@Composable
private fun PppUpText(@StringRes text: Int, clickEvent: () -> Unit) {
    MyText(
        text = stringResource(id = text),
        color = Gray10,
        fontSize = dpToSp(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                clickEvent()
            }
            .padding(start = 16.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
    )
}
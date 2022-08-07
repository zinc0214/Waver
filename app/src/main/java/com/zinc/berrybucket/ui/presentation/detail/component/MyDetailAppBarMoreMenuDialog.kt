package com.zinc.berrybucket.ui.presentation.detail.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.zinc.berrybucket.R
import com.zinc.berrybucket.ui.compose.theme.Gray1
import com.zinc.berrybucket.ui.compose.theme.Gray10
import com.zinc.berrybucket.util.dpToSp

@Composable
fun MyDetailAppBarMoreMenuDialog(optionPopUpShowed: MutableState<Boolean>) {

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
                    // TODO : Go To Edit
                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .sizeIn(
                        maxHeight = 36.dp
                    )
            ) {
                PppUpText(R.string.edit)
            }
            DropdownMenuItem(
                onClick = {
                    // TODO : Go To Count Change
                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .sizeIn(
                        maxHeight = 36.dp
                    )
            ) {
                PppUpText(R.string.countChange)
            }
            DropdownMenuItem(
                onClick = {
                    // TODO : Go To Delete
                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .sizeIn(
                        maxHeight = 36.dp
                    )
            ) {
                PppUpText(R.string.delete)
            }
        }
    }
}

@Composable
private fun PppUpText(@StringRes text: Int) {
    Text(
        text = stringResource(id = text),
        color = Gray10,
        fontSize = dpToSp(14.dp),
        modifier = Modifier.padding(start = 16.dp, end = 24.dp)
    )
}
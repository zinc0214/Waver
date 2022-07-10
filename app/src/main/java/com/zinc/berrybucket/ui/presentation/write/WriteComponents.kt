package com.zinc.berrybucket.ui.presentation.write

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.R
import com.zinc.berrybucket.ui.compose.theme.Gray3
import com.zinc.berrybucket.ui.presentation.common.IconButton

@Composable
fun WriteAppBar(
    modifier: Modifier,
    rightText: Int,
    clickEvent: (WriteAppBarClickEvent) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        val (closeButton, moreButton, divider) = createRefs()

        IconButton(
            image = R.drawable.btn40close,
            contentDescription = stringResource(id = R.string.closeDesc),
            modifier = Modifier
                .padding(start = 14.dp, top = 6.dp, bottom = 6.dp)
                .size(40.dp)
                .constrainAs(closeButton) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            onClick = {
                clickEvent(WriteAppBarClickEvent.CloseClicked)
            }
        )

        Text(
            modifier = Modifier
                .constrainAs(moreButton) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(start = 10.dp, end = 18.dp, top = 10.dp, bottom = 10.dp)
                .clickable {
                    clickEvent(WriteAppBarClickEvent.NextClicked)
                }
                .padding(start = 10.dp, end = 10.dp, top = 6.dp, bottom = 6.dp),
            text = stringResource(id = rightText)
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(divider) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }, color = Gray3
        )
    }

}

sealed class WriteAppBarClickEvent {
    object CloseClicked : WriteAppBarClickEvent()
    object NextClicked : WriteAppBarClickEvent()
}
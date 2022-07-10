package com.zinc.berrybucket.ui.presentation.write

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.R
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
        val (closeButton, moreButton) = createRefs()

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
            text = stringResource(id = rightText),
            modifier = Modifier
                .padding(end = 28.dp, top = 16.dp, bottom = 16.dp)
                .size(32.dp)
                .constrainAs(moreButton) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .clickable {
                    clickEvent(WriteAppBarClickEvent.NextClicked)
                }
        )
    }

}

sealed class WriteAppBarClickEvent {
    object CloseClicked : WriteAppBarClickEvent()
    object NextClicked : WriteAppBarClickEvent()
}
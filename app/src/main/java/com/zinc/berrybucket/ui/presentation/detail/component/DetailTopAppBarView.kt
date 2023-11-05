package com.zinc.berrybucket.ui.presentation.detail.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.DetailAppBarClickEvent
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray3
import com.zinc.berrybucket.ui.presentation.component.IconButton
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.util.dpToSp


@Composable
fun DetailTopAppBar(
    listState: LazyListState,
    titlePosition: Int,
    title: String,
    clickEvent: (DetailAppBarClickEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val isTitleScrolled =
        remember { derivedStateOf { listState.firstVisibleItemIndex } }.value > titlePosition

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        val (closeButton, titleView, moreButton, divider) = createRefs()

        IconButton(
            image = com.zinc.berrybucket.ui_common.R.drawable.btn_40_close,
            contentDescription = stringResource(id = com.zinc.berrybucket.ui_common.R.string.closeDesc),
            modifier = Modifier
                .padding(start = 14.dp, top = 6.dp, bottom = 6.dp)
                .size(40.dp)
                .constrainAs(closeButton) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            onClick = { clickEvent(DetailAppBarClickEvent.CloseClicked) }
        )

        if (isTitleScrolled) {
            MyText(
                color = Gray10,
                fontSize = dpToSp(16.dp),
                fontWeight = FontWeight.Bold,
                text = title,
                modifier = Modifier
                    .padding(top = 14.dp, bottom = 14.dp)
                    .constrainAs(titleView) {
                        start.linkTo(closeButton.end)
                        end.linkTo(moreButton.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }

        IconButton(
            image = R.drawable.btn32more,
            contentDescription = stringResource(id = com.zinc.berrybucket.ui_common.R.string.moreButtonDesc),
            modifier = Modifier
                .padding(end = 14.dp, top = 10.dp, bottom = 10.dp)
                .size(32.dp)
                .constrainAs(moreButton) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            onClick = { clickEvent(DetailAppBarClickEvent.MoreOptionClicked) }
        )

        if (isTitleScrolled) {
            Divider(
                modifier = Modifier.constrainAs(divider) {
                    top.linkTo(titleView.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                color = Gray3
            )
        }
    }
}

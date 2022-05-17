package com.zinc.berrybucket.compose.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Gray10
import com.zinc.berrybucket.model.DetailClickEvent


@Composable
fun DetailTopAppBar(
    listState: LazyListState,
    titlePosition: Int,
    title: String,
    clickEvent: (DetailClickEvent) -> Unit
) {

    val isTitleScrolled = listState.firstVisibleItemIndex > titlePosition

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        val (closeButton, titleView, moreButton) = createRefs()

        Icon(
            painter = painterResource(id = R.drawable.btn40close),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 14.dp, top = 6.dp, bottom = 6.dp)
                .clickable {
                    clickEvent(DetailClickEvent.CloseClicked)
                }
                .constrainAs(closeButton) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )

        if (isTitleScrolled) {
            Text(
                color = Gray10,
                fontSize = 16.sp,
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

        Icon(
            painter = painterResource(id = R.drawable.btn32more),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 14.dp, top = 6.dp, bottom = 6.dp)
                .clickable {
                    clickEvent(DetailClickEvent.MoreOptionClicked)
                }
                .constrainAs(moreButton) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

package com.zinc.berrybucket.ui.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray3
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_common.R

enum class TitleIconType {
    BACK, CLOSE
}

@Composable
fun TitleView(
    modifier: Modifier = Modifier,
    title: String,
    iconType: TitleIconType,
    isDividerVisible: Boolean,
    onIconClicked: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .background(color = Gray1)
            .height(52.dp)
    ) {
        val (backButton, titleTextView, bottomDivider) = createRefs()

        IconButton(
            onClick = { onIconClicked() },
            image = if (iconType == TitleIconType.BACK) R.drawable.btn_40_back else R.drawable.btn_40_close,
            modifier = Modifier
                .sizeIn(40.dp)
                .padding(start = 14.dp)
                .fillMaxHeight()
                .constrainAs(backButton) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            contentDescription = stringResource(id = if (iconType == TitleIconType.BACK) R.string.backDesc else R.string.closeDesc)
        )

        MyText(
            text = title,
            fontSize = dpToSp(16.dp),
            color = Gray10,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(titleTextView) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            fontWeight = FontWeight.Bold
        )


        if (isDividerVisible) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(bottomDivider) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                color = Gray3
            )
        }
    }
}

@Preview
@Composable
private fun TitlePreview() {
    TitleView(
        iconType = TitleIconType.BACK, onIconClicked = {},
        title = "타이틀",
        isDividerVisible = true
    )
}
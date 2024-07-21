package com.zinc.waver.ui.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_common.R

enum class TitleIconType {
    BACK, CLOSE
}

@Composable
fun TitleView(
    modifier: Modifier = Modifier,
    title: String,
    leftIconType: TitleIconType? = null,
    rightText: String? = null,
    rightTextEnable: Boolean = true,
    isDividerVisible: Boolean = false,
    onLeftIconClicked: (() -> Unit)? = null,
    onRightTextClicked: (() -> Unit)? = null

) {
    ConstraintLayout(
        modifier = modifier
            .background(color = Gray1)
            .height(52.dp)
    ) {
        val (leftButtonView, titleTextView, rightTextView, bottomDivider) = createRefs()

        if (leftIconType != null) {
            IconButton(
                onClick = { onLeftIconClicked?.let { it() } },
                image = if (leftIconType == TitleIconType.BACK) R.drawable.btn_40_back else R.drawable.btn_40_close,
                modifier = Modifier
                    .sizeIn(40.dp)
                    .padding(start = 14.dp)
                    .fillMaxHeight()
                    .constrainAs(leftButtonView) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                contentDescription = stringResource(id = if (leftIconType == TitleIconType.BACK) R.string.backDesc else R.string.closeDesc)
            )
        }


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

        if (rightText != null) {
            MyText(
                text = rightText,
                color = if (rightTextEnable) Main4 else Gray6,
                fontSize = dpToSp(dp = 18.dp),
                modifier = Modifier
                    .constrainAs(rightTextView) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(end = 18.dp)
                    .clickable { onRightTextClicked?.let { it() } }
                    .padding(start = 6.dp, end = 6.dp, top = 6.dp, bottom = 6.dp)
            )

        }

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
        leftIconType = TitleIconType.BACK, onLeftIconClicked = {},
        title = "타이틀",
        isDividerVisible = true
    )
}
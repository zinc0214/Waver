package com.zinc.berrybucket.ui.presentation.common

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.common.R
import com.zinc.berrybucket.model.BottomButtonClickEvent
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.util.dpToSp

@Composable
fun BottomButtonView(
    leftButtonEnable: Boolean = false,
    @StringRes leftText: Int = R.string.cancel,
    rightButtonEnable: Boolean = true,
    @StringRes rightText: Int = R.string.apply,
    clickEvent: (BottomButtonClickEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(color = Gray1)
    ) {
        Divider(
            modifier = Modifier
                .height(0.6.dp)
                .fillMaxWidth()
                .background(color = colorResource(id = R.color._dbdbdb))
        )

        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (leftContent, divider, rightContent) = createRefs()

            Divider(modifier = Modifier
                .fillMaxHeight()
                .width(0.6.dp)
                .background(
                    color = colorResource(
                        id = R.color._dbdbdb
                    )
                )
                .constrainAs(divider) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                })

            Box(
                modifier = Modifier
                    .constrainAs(leftContent) {
                        start.linkTo(parent.start)
                        end.linkTo(divider.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .fillMaxHeight()
                    .clickable {
                        clickEvent.invoke(BottomButtonClickEvent.LeftButtonClicked)
                    },

                ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = leftText),
                    color = if (leftButtonEnable) Main4 else Gray6,
                    textAlign = TextAlign.Center,
                    fontSize = dpToSp(16.dp)
                )
            }

            Box(
                modifier = Modifier
                    .constrainAs(rightContent) {
                        start.linkTo(divider.end)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
                    .fillMaxHeight()
                    .clickable {
                        clickEvent.invoke(BottomButtonClickEvent.RightButtonClicked)
                    }

            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = rightText),
                    color = if (rightButtonEnable) Main4 else Gray6,
                    textAlign = TextAlign.Center,
                    fontSize = dpToSp(16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun BottomButtonPreview() {
    BottomButtonView(
        leftButtonEnable = true,
        rightButtonEnable = false,
        clickEvent = {})
}
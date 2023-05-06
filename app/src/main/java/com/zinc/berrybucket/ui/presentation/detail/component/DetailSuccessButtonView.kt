package com.zinc.berrybucket.ui.presentation.detail.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.SuccessButtonInfo
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Main2
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.common.MyText
import com.zinc.berrybucket.ui.util.dpToSp

@Composable
fun DetailSuccessButtonView(
    modifier: Modifier = Modifier,
    successButtonInfo: SuccessButtonInfo,
    isWide: Boolean = false,
    successClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = if (isWide) 0.dp else 28.dp)
            .clip(RoundedCornerShape(if (isWide) 0.dp else 12.dp))
            .background(Main4)
            .clickable {
                successClicked.invoke()
            }
            .requiredHeightIn(min = if (isWide) 64.dp else 56.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (successButtonInfo.goalCount > 1) {
            CountSuccessButton(successButtonInfo)
        } else {
            SimpleSuccessButton()
        }
    }
}

@Composable
private fun SimpleSuccessButton(
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    MyText(
        text = stringResource(id = R.string.successButtonText),
        textAlign = textAlign,
        color = Gray1,
        fontWeight = FontWeight.Bold,
        fontSize = dpToSp(16.dp),
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun CountSuccessButton(successButtonInfo: SuccessButtonInfo) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val (titleView, userCountView, goalCountView) = createRefs()

        SimpleSuccessButton(modifier = Modifier
            .constrainAs(titleView) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(userCountView.start)
            }

            .padding(start = 24.dp),
            textAlign = TextAlign.Start)

        MyText(
            text = successButtonInfo.userCount.toString(),
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Bold,
            fontSize = dpToSp(16.dp),
            color = Gray1,
            modifier = Modifier
                .constrainAs(userCountView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(goalCountView.start)
                }
        )

        MyText(
            text = "/${successButtonInfo.goalCount}",
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Bold,
            fontSize = dpToSp(16.dp),
            color = Main2,
            modifier = Modifier
                .padding(end = 24.dp)
                .constrainAs(goalCountView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
        )
    }
}

@Preview
@Composable
fun CountSuccessButtonPreview() {
    CountSuccessButton(
        successButtonInfo = SuccessButtonInfo(
            goalCount = 1,
            userCount = 2
        )
    )
}
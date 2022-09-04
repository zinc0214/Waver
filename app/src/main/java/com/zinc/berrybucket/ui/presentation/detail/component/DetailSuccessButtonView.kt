package com.zinc.berrybucket.ui.presentation.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.text.HtmlCompat
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.SuccessButtonInfo
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Main4
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
            .requiredHeightIn(min = if (isWide) 64.dp else 56.dp),
        contentAlignment = Alignment.Center
    ) {
        if (successButtonInfo.goalCount > 0) {
            CountSuccessButton(successButtonInfo)
        } else {
            SimpleSuccessButton( )
        }
    }
}

@Composable
private fun SimpleSuccessButton(
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    Text(
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
        val (titleView, countView) = createRefs()

        SimpleSuccessButton(modifier = Modifier
            .constrainAs(titleView) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(countView.start)
            }

            .padding(start = 38.dp),
            textAlign = TextAlign.Start)

        val countText = stringResource(
            R.string.successButtonCount,
            successButtonInfo.userCount.toString(),
            successButtonInfo.goalCount.toString()
        )

        Text(
            text = HtmlCompat.fromHtml(
                countText,
                HtmlCompat.FROM_HTML_MODE_COMPACT
            ).toString(),
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Bold,
            fontSize = dpToSp(16.dp),
            modifier = Modifier
                .padding(end = 38.dp)
                .constrainAs(countView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
        )

    }
}
package com.zinc.berrybucket.ui.presentation.common

import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.R
import com.zinc.berrybucket.ui.compose.theme.Gray1
import com.zinc.berrybucket.ui.compose.theme.Gray10
import com.zinc.berrybucket.ui.compose.theme.Gray4
import com.zinc.berrybucket.ui.compose.theme.Main3


/**
 * 우측에 스위차가 있는 텍스트 뷰
 *
 * @param modifier
 * @param textLabel 좌측 텍스트 라벨
 * @param isChecked 현재 체크 되어있는지 확인
 * @param checkedChanged 체크 변경 이벤트 처리
 */
@Composable
fun LabelWithSwitchView(
    modifier: Modifier = Modifier,
    @StringRes textLabel: Int,
    isChecked: Boolean,
    checkedChanged: (Boolean) -> Unit
) {

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
            .padding(bottom = 10.dp)
    ) {

        val (leftContent, rightContent) = createRefs()
        Text(
            text = stringResource(id = textLabel),
            fontSize = 16.sp,
            color = Gray10,
            modifier = Modifier
                .constrainAs(leftContent) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(rightContent.start)
                    width = Dimension.fillToConstraints
                    linkTo(
                        parent.start,
                        rightContent.start,
                        startMargin = 0.dp,
                        endMargin = 14.dp,
                        bias = 0f
                    )
                }
        )

        Switch(
            modifier = Modifier.constrainAs(rightContent) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            },
            isChecked = isChecked,
            checkedChanged = checkedChanged
        )
    }
}

@Composable
private fun Switch(
    scale: Float = 2f,
    width: Dp = 50.dp,
    height: Dp = 30.dp,
    checkedTrackColor: Color = Main3,
    uncheckedTrackColor: Color = Gray4,
    gapBetweenThumbAndTrackEdge: Dp = 3.dp,
    modifier: Modifier,
    isChecked: Boolean,
    checkedChanged: (Boolean) -> Unit
) {

    val switchON = remember {
        mutableStateOf(isChecked) // Initially the switch is ON
    }

    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge

    // To move thumb, we need to calculate the position (along x axis)
    val animatePosition = animateFloatAsState(
        targetValue = if (switchON.value)
            with(LocalDensity.current) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() }
        else
            with(LocalDensity.current) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }
    )

    Canvas(
        modifier = modifier
            .size(width = width, height = height)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        // This is called when the user taps on the canvas
                        switchON.value = !switchON.value
                        checkedChanged(switchON.value)
                    }
                )
            }
            .background(
                color = if (switchON.value) checkedTrackColor else uncheckedTrackColor,
                shape = RoundedCornerShape(15.dp)
            )
    ) {
        // Thumb
        drawCircle(
            color = Gray1,
            radius = 12.dp.toPx(),
            center = Offset(
                x = animatePosition.value,
                y = size.height / 2
            )
        )
    }
}

@Preview
@Composable
private fun SwitchTest() {
    LabelWithSwitchView(
        modifier = Modifier,
        textLabel = R.string.allFilterImageViewDesc,
        isChecked = true,
        checkedChanged = {

        }
    )
}
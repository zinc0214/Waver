package com.zinc.waver.ui.presentation.component.calendar.date

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.zinc.waver.ui.presentation.component.MyText
import java.time.LocalDate

@Composable
internal fun SingleDate(
    day: Int,
    modifier: Modifier = Modifier,
    onDayPressed: ((LocalDate) -> Unit)?,
    localDate: LocalDate,
    dateBackgroundColour: Color,
    dateTextStyle: TextStyle,
    dateBackgroundShape: Shape,
    circleDiameter: Dp,
    isOutOfRange: Boolean,
) {
    val backgroundColor =
        when (dateBackgroundColour) {
            Color.Unspecified -> MaterialTheme.colors.background
            else -> dateBackgroundColour
        }

    val textStyle: TextStyle = dateTextStyle

    var dateModifier = modifier
        .background(color = backgroundColor, shape = dateBackgroundShape)

    if (onDayPressed != null && isOutOfRange.not()) {
        dateModifier = dateModifier.clickable {
            onDayPressed(localDate)
        }
    }

    Box(
        modifier = dateModifier
            .layout { measurable, constraints ->
                val resolvedCircleDiameterInt = circleDiameter.toPx().toInt()
                    .coerceIn(
                        constraints.minHeight,
                        minOf(constraints.maxWidth, constraints.maxHeight)
                    )

                val placeable = measurable.measure(
                    constraints.copy(
                        minWidth = resolvedCircleDiameterInt,
                        maxWidth = resolvedCircleDiameterInt,
                        minHeight = resolvedCircleDiameterInt,
                        maxHeight = resolvedCircleDiameterInt,
                    )
                )

                layout(placeable.width, placeable.width) {
                    placeable.place(x = 0, y = 0, zIndex = 0f)
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        if (isOutOfRange.not()) {
            MyText(
                textAlign = TextAlign.Center,
                modifier = Modifier,
                text = day.toString(),
                style = textStyle,
            )
        }
    }
}
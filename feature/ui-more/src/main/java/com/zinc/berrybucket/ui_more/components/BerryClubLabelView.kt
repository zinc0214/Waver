package com.zinc.berrybucket.ui_more.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.presentation.common.MyText
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_more.R

@Composable
internal fun BerryClubLabelView(enterClubClick: () -> Unit) {

    val colorStops = arrayOf(
        0.0f to Color(0xFFFC0D69),
        0.5f to Color(0xFFFD8597),
        1f to Color(0xFFFFC898)
    )

    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Gray1, contentColor = Gray1),
        contentPadding = PaddingValues(0.dp),
        onClick = {
            enterClubClick()
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colorStops = colorStops
                    )
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyText(
                text = stringResource(id = R.string.goToJoinBerryClub),
                color = Gray1,
                fontSize = dpToSp(dp = 16.dp),
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 28.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(com.zinc.berrybucket.ui_common.R.drawable.ico_16_right),
                contentDescription = null,
                modifier = Modifier
                    .sizeIn(16.dp)
                    .padding(end = 24.dp),
                colorFilter = ColorFilter.tint(Gray1)
            )
        }
    }
}

@Preview
@Composable
private fun BerryClubLabelPreview() {
    BerryClubLabelView {

    }
}
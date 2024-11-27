package com.zinc.waver.ui_more.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray4
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_more.R

@Composable
internal fun WaverClubLabelView(
    enterClubClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val colorStops = arrayOf(
        0.0f to Color(0xFF0061ff),
        0.8f to Color(0XFF329dfe),
        1f to Color(0xFF50c1fe)
    )

    Box(modifier = modifier.padding(top = 10.dp)) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Gray1, contentColor = Gray1),
            contentPadding = PaddingValues(0.dp),
            onClick = { enterClubClick() }
        ) {
            Box(modifier = Modifier.height(60.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colorStops = colorStops
                            )
                        )
                        .align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyText(
                        text = stringResource(id = R.string.goToJoinWaverClub),
                        color = Gray1,
                        fontSize = dpToSp(dp = 17.dp),
                        modifier = Modifier.padding(horizontal = 28.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(com.zinc.waver.ui_common.R.drawable.ico_16_right),
                        contentDescription = null,
                        modifier = Modifier
                            .sizeIn(16.dp)
                            .padding(end = 20.dp),
                        colorFilter = ColorFilter.tint(Gray1)
                    )
                }

                Row(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(R.drawable.img_banner_wave),
                        contentDescription = null,
                        modifier = Modifier.size(width = 198.dp, height = 60.dp)
                    )
                    Spacer(modifier = Modifier.width(9.dp))
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(R.drawable.img_banner_wave_2),
                        contentDescription = null,
                        modifier = Modifier.size(width = 111.dp, height = 60.dp)
                    )
                    Spacer(modifier = Modifier.width(42.dp))
                }
            }
        }

        MyText(
            modifier = Modifier
                .padding(start = 18.dp)
                .offset(y = (-20).dp)
                .background(
                    color = Gray1,
                    shape = RoundedCornerShape(18.dp)
                )
                .border(width = 1.dp, color = Gray4, shape = RoundedCornerShape(18.dp))
                .padding(start = 10.dp, top = 3.dp, bottom = 1.dp, end = 10.dp),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Gray9, fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.waverPlusBenefitDesc1))
                }
                withStyle(style = SpanStyle(color = Main4, fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.waverPlusBenefitDesc2))
                }
            },
            fontSize = dpToSp(dp = 14.dp)
        )
    }

}

@Preview
@Composable
private fun WaverClubLabelPreview() {
    WaverClubLabelView(modifier = Modifier.padding(top = 40.dp), enterClubClick = {

    })
}
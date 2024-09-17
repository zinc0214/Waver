package com.zinc.waver.ui_more.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.design.theme.Gray8
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.design.theme.Main1
import com.zinc.waver.ui.design.theme.Main2
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.design.theme.Sub_D3
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.HtmlText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_more.R
import com.zinc.waver.ui_more.models.WavePlusInfo

@Composable
internal fun WavePlusPayView(info: WavePlusInfo, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        WavePlusPayTitleView()

        Spacer(modifier = Modifier.height(26.dp))

        WavePlusPayYearView(info)

        Spacer(modifier = Modifier.height(16.dp))

        WavePlusPayMonthView(info)
    }

}

@Composable
fun WavePlusPayTitleView(modifier: Modifier = Modifier) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val (logo1, title1, title2, title3, divider, logo2, logo3) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.img_wave_04),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 2.dp, top = 5.dp)
                .size(30.dp)
                .constrainAs(logo1) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        MyText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Sub_D3, fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.payTitle1))
                }
                withStyle(style = SpanStyle(color = Gray9, fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.payTitle2))
                }
            },
            fontSize = dpToSp(dp = 22.dp),
            modifier = Modifier
                .padding(top = 27.dp)
                .constrainAs(title1) {
                    start.linkTo(logo1.end)
                    top.linkTo(parent.top)
                })

        MyText(
            text = stringResource(id = R.string.payTitle3),
            color = Main4,
            fontWeight = FontWeight.Bold,
            fontSize = dpToSp(dp = 22.dp),
            modifier = Modifier.constrainAs(title2) {
                top.linkTo(title1.bottom)
                start.linkTo(title1.start)
            })

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(divider) {
                    start.linkTo(title2.start)
                    end.linkTo(title2.end)
                    top.linkTo(title2.bottom)
                    width = Dimension.fillToConstraints
                }, thickness = 2.dp, color = Main4
        )

        MyText(
            text = " " + stringResource(id = R.string.payTitle4),
            color = Gray9,
            fontWeight = FontWeight.Bold,
            fontSize = dpToSp(dp = 22.dp),
            modifier = Modifier.constrainAs(title3) {
                top.linkTo(title2.top)
                start.linkTo(title2.end)
            })


        Image(
            painter = painterResource(id = R.drawable.img_wave_06),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding(bottom = 5.dp)
                .constrainAs(logo2) {
                    top.linkTo(parent.top)
                    start.linkTo(title1.end)
                    bottom.linkTo(title2.top)
                },
            contentScale = ContentScale.FillWidth
        )

        Image(
            painter = painterResource(id = R.drawable.img_wave_05),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 6.dp, top = 55.dp)
                .size(40.dp)
                .constrainAs(logo3) {
                    start.linkTo(title3.end)
                    top.linkTo(parent.top)
                },
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
private fun WavePlusPayYearView(info: WavePlusInfo, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .background(color = Gray1, shape = RoundedCornerShape(8.dp))
            .border(width = 0.6.dp, shape = RoundedCornerShape(8.dp), color = Main2)
    ) {
        HtmlText(
            html = stringResource(id = R.string.yearPayText),
            fontSize = 15.dp,
            textColor = Gray10.hashCode(),
            modifier = Modifier
                .padding(top = 14.dp, bottom = 12.dp)
                .align(Alignment.CenterHorizontally)
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = Gray3
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyText(
                text = info.yearBenefitText,
                color = Main4,
                fontWeight = FontWeight.SemiBold,
                fontSize = dpToSp(
                    dp = 14.dp
                )
            )

            MyText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Gray9, fontWeight = FontWeight.Bold)) {
                        append(stringResource(id = R.string.payPerMonth) + " ")
                    }
                    withStyle(style = SpanStyle(color = Main4, fontWeight = FontWeight.Bold)) {
                        append(info.yearPerPay)
                    }
                },
                fontWeight = FontWeight.SemiBold,
                fontSize = dpToSp(
                    dp = 14.dp
                ),
                modifier = Modifier
                    .padding(start = 8.5.dp)
                    .background(
                        color = Main1, shape = RoundedCornerShape(4.dp)
                    )
                    .padding(start = 10.dp, end = 10.dp, top = 1.dp, bottom = 3.dp)
            )
        }

        MyText(
            text = info.yearPay,
            color = Gray1,
            fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Main4,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 8.dp,
                        bottomEnd = 8.dp
                    )
                )
                .padding(top = 9.dp, bottom = 10.dp)
        )
    }
}

@Composable
fun WavePlusPayMonthView(info: WavePlusInfo, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .background(color = Gray1, shape = RoundedCornerShape(8.dp))
            .border(width = 0.6.dp, shape = RoundedCornerShape(8.dp), color = Gray3)
    ) {
        HtmlText(
            html = stringResource(id = R.string.monthPayText),
            fontSize = 15.dp,
            textColor = Gray10.hashCode(),
            modifier = Modifier
                .padding(top = 14.dp, bottom = 12.dp)
                .align(Alignment.CenterHorizontally)
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = Gray3
        )

        MyText(
            text = info.monthBenefitText,
            textAlign = TextAlign.Center,
            color = Gray9,
            fontSize = dpToSp(dp = 14.dp),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp, bottom = 18.dp)
        )


        MyText(
            text = info.monthPay,
            color = Gray8,
            fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Gray2,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 8.dp,
                        bottomEnd = 8.dp
                    )
                )
                .padding(top = 9.dp, bottom = 10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WavePlusPayPreView() {
    WavePlusPayView(
        info = WavePlusInfo(
            options = listOf(),
            yearPerPay = "5,000원",
            yearBenefitText = "1주일 무료 + 웨이버 플러스",
            yearPay = "20,000원",
            monthPay = "4,000원",
            monthBenefitText = "웨이버 플러스"
        )
    )
}
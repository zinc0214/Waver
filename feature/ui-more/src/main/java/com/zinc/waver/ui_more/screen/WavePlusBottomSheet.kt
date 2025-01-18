package com.zinc.waver.ui_more.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray4
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.theme.Gray7
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.design.theme.Main5
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.model.WaverPlusType
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_more.R
import com.zinc.waver.ui_more.models.WaverPlusBottomOption


@Composable
fun WavePlusBottomSheet(modifier: Modifier = Modifier, selectedItem: (WaverPlusType) -> Unit) {
    val optionList = listOf(
        WaverPlusBottomOption(
            type = WaverPlusType.YEAR,
            title = stringResource(R.string.waverPlusBottomSheetYear1),
            content = stringResource(R.string.waverPlusBottomSheetYear2),
            subContent = stringResource(R.string.waverPlusBottomSheetYear3)

        ),
        WaverPlusBottomOption(
            type = WaverPlusType.MONTH,
            title = stringResource(R.string.waverPlusBottomSheetMonth1),
            content = stringResource(R.string.waverPlusBottomSheetMonth2),
            subContent = ""
        )
    )

    var currentSelectedItem by remember { mutableStateOf(optionList[0]) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Gray1)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, top = 32.dp, bottom = 30.dp)
                .verticalScroll(rememberScrollState())
        ) {
            MyText(
                modifier = Modifier.padding(start = 2.dp),
                text = stringResource(R.string.wavePlus),
                color = Main4,
                fontSize = dpToSp(18.dp),
                fontWeight = FontWeight.SemiBold
            )

            MyText(
                modifier = Modifier.padding(start = 2.dp, bottom = 20.dp),
                text = stringResource(R.string.waverPlusBottomSheetDesc),
                color = Gray7,
                fontSize = dpToSp(15.dp),
            )

            optionList.forEach { option ->
                WaverPlusOption(
                    option = option,
                    isSelected = option == currentSelectedItem,
                    selected = {
                        currentSelectedItem = it
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            DescView(text = stringResource(R.string.waverPlusBottomSheetDesc1))
            DescView(text = stringResource(R.string.waverPlusBottomSheetDesc2))
            DescView(text = stringResource(R.string.waverPlusBottomSheetDesc3))
            DescView(text = stringResource(R.string.waverPlusBottomSheetDesc4))

            Spacer(modifier = Modifier.height(22.dp))
        }

        MyText(
            text = currentSelectedItem.title + " " + stringResource(R.string.waverPlusBottomSheetButton),
            fontSize = dpToSp(18.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Main4)
                .clickable {
                    selectedItem(currentSelectedItem.type)
                }
                .padding(horizontal = 52.dp, vertical = 17.dp),
            color = Gray1
        )
    }
}

@Composable
private fun WaverPlusOption(
    option: WaverPlusBottomOption,
    isSelected: Boolean,
    selected: (WaverPlusBottomOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val strokeColor = if (isSelected) Main4 else Gray4
    val strokeWidth = if (isSelected) 1.dp else 0.6.dp
    val titleColor = if (isSelected) Main4 else Gray6
    val subColor = if (isSelected) Gray9 else Gray6

    Row(
        modifier = modifier
            .heightIn(min = 80.dp)
            .fillMaxWidth()
            .background(color = Gray1, shape = RoundedCornerShape(8.dp))
            .border(width = strokeWidth, color = strokeColor, shape = RoundedCornerShape(8.dp))
            .clickable {
                selected(option)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyText(
            modifier = Modifier.padding(start = 24.dp),
            text = option.title,
            color = titleColor,
            fontSize = dpToSp(16.dp),
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .padding(end = 20.dp)
        ) {
            MyText(
                text = option.content,
                fontSize = dpToSp(18.dp),
                color = subColor,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
            if (option.subContent.isNotEmpty()) {
                MyText(
                    modifier = Modifier.fillMaxWidth(),
                    text = option.subContent,
                    fontSize = dpToSp(15.dp),
                    color = Gray6,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
private fun DescView(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, start = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val highlightText = stringResource(R.string.waverPlusBottomSheetColorText)
        val annotatedText = buildAnnotatedString {
            // "다라"의 시작 위치를 찾습니다.
            val startIndex = text.indexOf(highlightText)

            if (startIndex != -1) {
                // "다라" 이전의 텍스트 추가
                append(text.substring(0, startIndex))

                // "다라"에 스타일 추가
                withStyle(style = SpanStyle(color = Main5)) {
                    append(highlightText)
                }

                // "다라" 이후의 텍스트 추가
                append(text.substring(startIndex + highlightText.length))
            } else {
                // "다라"가 없을 경우 전체 텍스트 그대로 추가
                append(text)
            }
        }


        Box(
            modifier = Modifier
                .background(color = Gray6, shape = CircleShape)
                .size(4.dp)
        )
        MyText(
            text = annotatedText,
            color = Gray6,
            fontSize = dpToSp(12.dp),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WavePlusBottomSheetPreview() {
    WavePlusBottomSheet(selectedItem = {})
}
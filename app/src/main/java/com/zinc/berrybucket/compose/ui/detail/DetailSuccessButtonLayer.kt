package com.zinc.berrybucket.compose.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Gray1
import com.zinc.berrybucket.compose.theme.Main4

@Composable
fun DetailSuccessButtonView(
    modifier: Modifier = Modifier,
    successClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(start = 28.dp, end = 28.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Main4)
            .clickable {
                successClicked.invoke()
            }
    ) {
        Text(
            text = stringResource(id = R.string.successButtonText),
            textAlign = TextAlign.Center,
            color = Gray1,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(vertical = 14.dp, horizontal = 24.dp)
                .fillMaxWidth()
        )
    }
}
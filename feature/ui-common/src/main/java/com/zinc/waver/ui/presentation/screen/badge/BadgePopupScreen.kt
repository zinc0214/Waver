package com.zinc.waver.ui.presentation.screen.badge

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.design.theme.Gray5
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.model.BadgePopupInfo
import com.zinc.waver.ui.presentation.screen.ads.AdBannerScreen
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_common.R

@Composable
fun BadgePopupScreen(info: BadgePopupInfo, onDismissRequest: () -> Unit) {
    val painter = rememberAsyncImagePainter(
        model = info.badgeUrl,
        placeholder = painterResource(R.drawable.badge_placeholder),
        error = painterResource(R.drawable.badge_placeholder)
    )

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .width(320.dp)
                .padding(20.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color = Gray1),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painter,
                contentDescription = stringResource(id = R.string.bucketImage),
                modifier = Modifier
                    .width(320.dp),
                contentScale = ContentScale.Crop
            )

            MyText(
                text = stringResource(R.string.badgeAchieveText),
                color = Gray9,
                fontWeight = FontWeight.SemiBold,
                fontSize = dpToSp(14.dp),
                modifier = Modifier.padding(top = 18.dp)
            )

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .background(color = Gray2, shape = RoundedCornerShape(15.dp))
                    .border(width = 1.dp, color = Gray3, shape = RoundedCornerShape(15.dp))
                    .padding(top = 6.dp, bottom = 8.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MyText(
                    text = info.badgeText,
                    color = Main4,
                    fontSize = dpToSp(18.dp),
                    fontWeight = FontWeight.SemiBold
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .background(Gray5)
                        .height(12.dp)
                        .width(1.dp)
                )
                MyText(
                    text = info.badgeGrade + stringResource(R.string.badgeAchieveGrade),
                    color = Gray9,
                    fontSize = dpToSp(18.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.padding(top = 9.dp))

            AdBannerScreen()

            MyText(
                text = stringResource(R.string.confirm),
                color = Gray10,
                textAlign = TextAlign.Center,
                fontSize = dpToSp(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = { onDismissRequest() },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple()
                    )
                    .padding(top = 14.dp, bottom = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BadgePopupScreenPreview() {
    BadgePopupScreen(
        info = BadgePopupInfo(badgeUrl = "badgeUrl", badgeText = "badgeText", badgeGrade = "1"),
        onDismissRequest = {}
    )
}
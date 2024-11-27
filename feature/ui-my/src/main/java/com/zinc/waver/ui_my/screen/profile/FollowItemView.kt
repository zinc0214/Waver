package com.zinc.waver.ui_my.screen.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zinc.common.models.OtherProfileInfo
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_common.R

@Composable
fun FollowItemView(
    modifier: Modifier,
    info: OtherProfileInfo,
    goToOtherHome: (String) -> Unit
) {
    Row(
        modifier = modifier.clickable {
            goToOtherHome(info.id)
        },
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(info.imgUrl)
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.testimg),
            placeholder = painterResource(R.drawable.testimg),
            contentDescription = stringResource(R.string.profileImageDesc),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp, 40.dp)
                .aspectRatio(1f)
                .clip(shape = RoundedCornerShape(14.dp))
                .border(width = 1.dp, color = Gray2, shape = RoundedCornerShape(14.dp))
        )

        MyText(
            modifier = Modifier.padding(start = 12.dp),
            text = info.name,
            color = Gray9,
            fontSize = dpToSp(15.dp),
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
private fun FollowItemPreview() {
    FollowItemView(
        modifier = Modifier.wrapContentWidth(),
        info = OtherProfileInfo(
            id = "1",
            imgUrl = "",
            name = "프로필의 이름이 길면 점점점이 노출되어야 하니까 테스트 해볼까? ",
            mutualFollow = false
        ),
        {}
    )
}
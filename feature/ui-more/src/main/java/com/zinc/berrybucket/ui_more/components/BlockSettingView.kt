package com.zinc.berrybucket.ui_more.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray4
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.design.theme.Gray9
import com.zinc.berrybucket.ui.presentation.common.MyText
import com.zinc.berrybucket.ui.presentation.common.TitleIconType
import com.zinc.berrybucket.ui.presentation.common.TitleView
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_more.R
import com.zinc.berrybucket.ui_more.models.BlockMemberData

@Composable
internal fun BlockTitle(backClicked: () -> Unit) {
    TitleView(
        title = stringResource(id = R.string.blockSettingTitle),
        leftIconType = TitleIconType.BACK,
        isDividerVisible = true,
        onLeftIconClicked = {
            backClicked()
        }
    )
}

@Composable
internal fun BlockMemberView(memberData: BlockMemberData) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 20.dp, top = 12.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = R.drawable.test),
            contentDescription = stringResource(
                id = R.string.moreProfileImageDesc
            ),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(32.dp, 32.dp)
                .aspectRatio(1f)
                .clip(shape = RoundedCornerShape(12.dp))
        )

        MyText(
            text = memberData.nickName,
            modifier = Modifier.padding(start = 12.dp),
            color = Gray9,
            fontSize = dpToSp(
                dp = 14.dp
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        MyText(
            text = stringResource(id = R.string.nonBlocking),
            color = Gray6,
            fontSize = dpToSp(dp = 12.dp),
            modifier = Modifier
                .padding(start = 10.dp)
                .background(shape = RoundedCornerShape(15.dp), color = Gray1)
                .border(width = 1.dp, shape = RoundedCornerShape(15.dp), color = Gray4)
                .clip(shape = RoundedCornerShape(15.dp))
                .clickable(onClick = {

                }, role = Role.Button)
                .padding(horizontal = 16.dp, vertical = 7.dp)
        )
    }
}
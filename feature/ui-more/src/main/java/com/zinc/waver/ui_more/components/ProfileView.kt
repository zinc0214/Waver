package com.zinc.waver.ui_more.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.design.theme.Gray4
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.theme.Gray7
import com.zinc.waver.ui.design.theme.Main3
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_more.R
import com.zinc.waver.ui_more.models.UIMoreMyProfileInfo
import com.zinc.waver.ui_common.R as CommonR

@Composable
internal fun MoreTopProfileView(info: UIMoreMyProfileInfo, goToProfileUpdate: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .padding(28.dp)
            .fillMaxWidth()
    ) {
        val (textView, imageView, buttonView) = createRefs()

        ProfileTextView(
            modifier = Modifier.constrainAs(textView) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(imageView.start)
                width = Dimension.fillToConstraints
            },
            info = info
        )

        ProfileImageView(modifier = Modifier.constrainAs(imageView) {
            top.linkTo(parent.top)
            end.linkTo(parent.end)
        }, profileUrl = info.imgUrl)

        ProfileUpdateButtonView(modifier = Modifier
            .constrainAs(buttonView) {
                top.linkTo(imageView.bottom, 20.dp)
                top.linkTo(textView.bottom, 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
            .fillMaxWidth()) { goToProfileUpdate() }
    }
}

@Composable
private fun ProfileTextView(modifier: Modifier, info: UIMoreMyProfileInfo) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyText(
                text = info.badgeTitle,
                color = Main3,
                fontSize = dpToSp(dp = 14.dp),
                modifier = Modifier.padding(end = 8.dp)
            )

            MyText(
                text = stringResource(id = R.string.goToBadgeChangeButtonText),
                color = Gray6,
                fontSize = dpToSp(dp = 12.dp),
                modifier = Modifier
                    .background(shape = RoundedCornerShape(12.dp), color = Gray2)
                    .border(width = 1.dp, shape = RoundedCornerShape(12.dp), color = Gray3)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .clickable(onClick = {

                    }, role = Role.Button)
                    .padding(horizontal = 9.dp, vertical = 4.dp)
            )
        }

        MyText(
            text = info.name, color = Gray10, fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )

        MyText(
            text = info.bio, color = Gray7, fontSize = dpToSp(dp = 14.dp),
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}


@Composable
private fun ProfileImageView(modifier: Modifier, profileUrl: String) {
    Image(
        painter = rememberAsyncImagePainter(
            model = profileUrl,
            placeholder = painterResource(com.zinc.waver.ui_common.R.drawable.kakao),
            error = painterResource(com.zinc.waver.ui_common.R.drawable.kakao)
        ),
        contentDescription = stringResource(
            id = CommonR.string.moreProfileImageDesc
        ),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(70.dp, 70.dp)
            .aspectRatio(1f)
            .clip(shape = RoundedCornerShape(28.dp))
    )
}

@Composable
private fun ProfileUpdateButtonView(modifier: Modifier, goToUpdate: () -> Unit) {
    Button(
        onClick = { goToUpdate() },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Gray4),
        colors = ButtonDefaults.buttonColors(backgroundColor = Gray1, contentColor = Gray7),
        contentPadding = PaddingValues(vertical = 12.dp),
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
    ) {
        MyText(
            text = stringResource(id = R.string.goToUpdateProfile),
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = dpToSp(dp = 14.dp)
        )
    }
}

@Preview
@Composable
private fun ProfileTextPreView() {
    ProfileTextView(
        info = UIMoreMyProfileInfo(
            name = "한아라고",
            imgUrl = "",
            badgeUrl = "",
            badgeTitle = "이제 버킷리스트를 시작한",
            bio = "나는 나는 멋쟁이 토마통"
        ),
        modifier = Modifier
    )
}

@Preview
@Composable
private fun ProfileImagePreview() {
    ProfileImageView(modifier = Modifier, "")
}

@Preview
@Composable
private fun ProfilePreview() {
    MoreTopProfileView(
        info = UIMoreMyProfileInfo(
            name = "한아라고",
            imgUrl = "",
            badgeUrl = "BadgeType.TRIP1",
            badgeTitle = "이제 버킷리스트를 시작한",
            bio = "나는 나는 멋쟁이 토마통"
        ),
        {}
    )
}
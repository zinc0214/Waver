package com.zinc.berrybucket.ui.presentation.write

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.flowlayout.FlowRow
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.WriteAddOption
import com.zinc.berrybucket.model.WriteFriend
import com.zinc.berrybucket.ui.compose.theme.*
import com.zinc.berrybucket.ui.presentation.common.IconButton
import com.zinc.berrybucket.util.dpToSp

@Composable
fun WriteTitleView(
    modifier: Modifier = Modifier, title: String
) {
    Text(
        modifier = modifier
            .padding(start = 28.dp, top = 28.dp, end = 28.dp)
            .fillMaxWidth(),
        text = title,
        fontSize = dpToSp(24.dp),
        color = Gray10
    )
}

@Composable
fun WriteAddOptionView(
    modifier: Modifier,
    option: WriteAddOption,
    isLastItem: Boolean,
    optionClicked: () -> Unit
) {

    Column(modifier = modifier.clickable { optionClicked() }) {
        Divider(color = Gray3)

        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (title, arrow, tag) = createRefs()

            Image(
                modifier = Modifier
                    .constrainAs(arrow) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                    .padding(20.dp),
                painter = painterResource(R.drawable.ico_16_right),
                contentDescription = null)

            Text(
                text = option.title,
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(arrow.start)
                        bottom.linkTo(tag.top)
                        width = Dimension.fillToConstraints
                    }
                    .padding(
                        start = 28.dp,
                        top = 18.dp,
                        end = 28.dp,
                        bottom = if (option.tagList.isEmpty()) 18.dp else 12.dp
                    ),
                color = Gray10,
                fontSize = dpToSp(16.dp))

            FlowRow(
                modifier = Modifier
                    .constrainAs(tag) {
                        top.linkTo(title.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(arrow.start)
                        width = Dimension.fillToConstraints
                    }
                    .padding(
                        start = if (option.tagList.isEmpty()) 0.dp else 28.dp,
                        bottom = if (option.tagList.isEmpty()) 0.dp else 20.dp
                    ),
                mainAxisSpacing = 12.dp,
                crossAxisSpacing = 8.dp,
            ) {
                option.tagList.forEach {
                    Text(
                        text = it,
                        modifier = Modifier,
                        color = Main3,
                        fontSize = dpToSp(16.dp),
                    )
                }
            }
        }

        if (isLastItem) {
            Divider(color = Gray3)
        }
    }
}

@Composable
fun AddedFriendItem(
    writeFriend: WriteFriend,
    deleteFriend: (WriteFriend) -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                color = Gray1,
                shape = RoundedCornerShape(18.dp)
            )
            .border(width = 1.dp, shape = RoundedCornerShape(18.dp), color = Gray4),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = writeFriend.nickname,
            color = Gray9,
            fontSize = dpToSp(14.dp),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 10.dp, end = 4.dp)
        )

        IconButton(
            modifier = Modifier
                .padding(8.dp)
                .then(Modifier.size(20.dp)),
            onClick = {
                deleteFriend(writeFriend)
            },
            image = R.drawable.btn_20_delete,
            contentDescription = stringResource(id = R.string.deleteButtonDesc)
        )
    }
}

@Composable
fun ShowAllFriendItem(
    clicked: () -> Unit
) {
    Text(
        text = stringResource(id = R.string.showSelectedAllFriends),
        color = Main3,
        fontSize = dpToSp(14.dp),
        modifier = Modifier
            .background(
                color = Gray1,
                shape = RoundedCornerShape(18.dp)
            )
            .border(width = 1.dp, color = Main2, shape = RoundedCornerShape(18.dp))
            .padding(horizontal = 22.dp, vertical = 10.dp)
            .clickable {
                clicked()
            }
    )
}

@Composable
fun WriteSelectFriendItem(
    modifier: Modifier = Modifier,
    writeFriend: WriteFriend,
    isSelected: Boolean
) {
    Card(
        backgroundColor = Gray1,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, if (isSelected) Main3 else Gray3),
        elevation = 0.dp,
        modifier = modifier
    ) {

        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (profileImage, nickNameView) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.kakao),
                contentDescription = stringResource(
                    id = R.string.feedProfileImage
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(32.dp)
                    .aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .constrainAs(profileImage) {
                        linkTo(
                            top = parent.top,
                            bottom = parent.bottom,
                            topMargin = 16.dp,
                            bottomMargin = 16.dp
                        )
                        start.linkTo(parent.start)
                    }
            )

            Text(
                text = writeFriend.nickname,
                fontSize = dpToSp(14.dp),
                color = Gray10,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.constrainAs(nickNameView) {
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom,
                        topMargin = 22.dp,
                        bottomMargin = 22.dp
                    )
                    linkTo(
                        start = profileImage.end,
                        end = parent.end,
                        startMargin = 12.dp,
                        endMargin = 12.dp
                    )
                    width = Dimension.fillToConstraints
                })
        }
    }
}
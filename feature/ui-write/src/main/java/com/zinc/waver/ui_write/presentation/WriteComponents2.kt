package com.zinc.waver.ui_write.presentation

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.flowlayout.FlowRow
import com.zinc.waver.model.WriteAddOption
import com.zinc.waver.model.WriteFriend
import com.zinc.waver.model.WriteOpenType
import com.zinc.waver.model.WriteOptionsType2
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray11
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.design.theme.Gray4
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.theme.Gray7
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.design.theme.Main2
import com.zinc.waver.ui.design.theme.Main3
import com.zinc.waver.ui.presentation.component.IconButton
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.Switch
import com.zinc.waver.ui.presentation.component.SwitchOnlyView
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_write.R
import com.zinc.waver.ui_common.R as CommonR

@Composable
fun WriteTitleView(
    modifier: Modifier = Modifier, title: String
) {
    MyText(
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
    optionClicked: () -> Unit,
    optionValueChanged: (WriteAddOption) -> Unit
) {

    Column(modifier = modifier.clickable { optionClicked() }) {

        Divider(color = Gray3)

        if (option.showDivider) {
            Divider(color = Gray2, thickness = 8.dp)
            Divider(color = Gray3)
        }

        if (option.type is WriteOptionsType2.SCRAP) {
            val scrapOption = option.type as WriteOptionsType2.SCRAP
            WriteScrapOptionView(
                modifier = Modifier.fillMaxWidth(),
                isScrapAvailable = scrapOption.isScrapAvailable,
                isScrapUsed = scrapOption.isScrapUsed,
                scrapChanged = { isScrapUsed ->
                    scrapOption.isScrapUsed = isScrapUsed
                    optionValueChanged(
                        option.copy {
                            (it as WriteOptionsType2.SCRAP).isScrapUsed = isScrapUsed
                        }
                    )
                })
        } else {
            TextWithTagOptionView(option)
        }

        if (isLastItem) {
            Divider(color = Gray3)
        }
    }
}

@Composable
private fun TextWithTagOptionView(option: WriteAddOption) {
    val isValid = when (val type = option.type) {
        is WriteOptionsType2.FRIENDS -> type.isUsable
        else -> true
    }

    val arrowColor = if (isValid) Gray11 else Gray4
    val textColor = if (isValid) Gray10 else Gray6

    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (title, arrow, tag) = createRefs()
        Image(
            modifier = Modifier
                .constrainAs(arrow) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .padding(20.dp),
            painter = painterResource(CommonR.drawable.ico_16_right),
            colorFilter = ColorFilter.tint(arrowColor),
            contentDescription = null)



        MyText(
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
                    bottom = if (option.showList.isEmpty()) 18.dp else 12.dp
                ),
            color = textColor,
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
                    start = if (option.showList.isEmpty()) 0.dp else 28.dp,
                    bottom = if (option.showList.isEmpty()) 0.dp else 20.dp
                ),
            mainAxisSpacing = 12.dp,
            crossAxisSpacing = 8.dp,
        ) {
            option.showList.forEach {
                MyText(
                    text = it,
                    modifier = Modifier,
                    color = Main3,
                    fontSize = dpToSp(16.dp),
                )
            }
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
        MyText(
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
            image = CommonR.drawable.btn_20_delete,
            contentDescription = stringResource(id = com.zinc.waver.ui_common.R.string.deleteButtonDesc)
        )
    }
}

@Composable
fun ShowAllFriendItem(
    clicked: () -> Unit
) {
    MyText(
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
                painter = painterResource(id = com.zinc.waver.ui_common.R.drawable.testimg),
                contentDescription = stringResource(
                    id = CommonR.string.profileImageDesc
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

            MyText(
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

@Composable
fun SelectOpenTypePopup(
    isPrivateAvailable: Boolean,
    onDismissRequest: () -> Unit,
    typeSelected: (WriteOpenType) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(8.dp),
            color = Gray1
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp, bottom = 22.dp)
            ) {

                MyText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    text = stringResource(id = R.string.optionOpenType),
                    color = Gray10,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                MyText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .clickable {
                            typeSelected(WriteOpenType.PUBLIC)
                        },
                    text = stringResource(id = R.string.optionOpenTypePublic),
                    color = Gray10,
                    fontSize = 14.sp
                )

                MyText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .clickable {
                            typeSelected(WriteOpenType.FRIENDS_OPEN)
                        },
                    text = stringResource(id = R.string.optionOpenTypeFriends),
                    color = Gray10,
                    fontSize = 14.sp
                )

                if (isPrivateAvailable) {
                    MyText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .clickable {
                                typeSelected(WriteOpenType.PRIVATE)
                            },
                        text = stringResource(id = R.string.optionOpenTypePrivate),
                        color = Gray10,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}


@Composable
fun WriteScrapOptionView(
    modifier: Modifier,
    isScrapAvailable: Boolean,
    isScrapUsed: Boolean,
    scrapChanged: (Boolean) -> Unit
) {
    val context = LocalContext.current

    Column(modifier = modifier) {

        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (title, switch) = createRefs()

            Row(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(
                        start = 28.dp,
                        top = 18.dp,
                        end = 28.dp,
                        bottom = 18.dp
                    ),
                verticalAlignment = Alignment.CenterVertically) {
                MyText(
                    text = stringResource(id = R.string.optionScrap),
                    color = if (isScrapAvailable) Gray10 else Gray7,
                    fontSize = dpToSp(16.dp)
                )

                IconButton(
                    modifier = Modifier
                        .size(16.dp)
                        .padding(start = 2.dp),
                    onClick = {

                    },
                    image = R.drawable.btn_16_info,
                    contentDescription = stringResource(id = R.string.optionScrapInfoDesc)
                )
            }

            if (isScrapAvailable) {
                Switch(
                    modifier = Modifier
                        .constrainAs(switch) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(vertical = 12.5.dp)
                        .padding(end = 20.dp),
                    isSwitchOn = isScrapUsed,
                    switchChanged = {
                        scrapChanged(it)
                    }
                )
            } else {
                SwitchOnlyView(
                    modifier = Modifier
                        .constrainAs(switch) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(vertical = 12.5.dp)
                        .padding(end = 20.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    Toast
                                        .makeText(
                                            context,
                                            R.string.optionScrapNotAvailable,
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            )
                        }
                )
            }


        }
    }
}

@Preview
@Composable
private fun SelectOpenTypePopupPreview() {
    SelectOpenTypePopup(true, {}) {

    }
}

//@Preview
//@Composable
//private fun WriteScrapOptionPreview() {
//    WriteScrapOptionView(
//        modifier = Modifier, isScrapAvailable = false,
//        isScrapUsed = true,
//        {},
//        option
//    )
//}
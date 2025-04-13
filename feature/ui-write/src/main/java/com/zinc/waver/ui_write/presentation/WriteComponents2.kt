package com.zinc.waver.ui_write.presentation

import android.widget.Toast
import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TooltipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.zinc.waver.model.WriteFriend
import com.zinc.waver.model.WriteKeyWord
import com.zinc.waver.model.WriteOpenType
import com.zinc.waver.model.WriteOptionsType2
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
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
import com.zinc.waver.ui.util.HtmlText2
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_write.R
import kotlinx.coroutines.launch
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun KeywordsOptionView(
    modifier: Modifier,
    keywordList: List<WriteKeyWord>,
    optionClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { optionClicked() }
    ) {
        Divider(color = Gray4)

        OptionTextView(
            modifier = Modifier.padding(top = 18.dp),
            text = stringResource(R.string.optionKeywordAddTitle)
        )

        if (keywordList.isNotEmpty()) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 18.dp, start = 28.dp, end = 56.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                keywordList.forEach { keyword ->
                    MyText(text = "#${keyword.text}", color = Main3, fontSize = dpToSp(16.dp))
                }
            }
        } else {
            Spacer(modifier = Modifier.padding(bottom = 18.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FriendsOptionView(
    modifier: Modifier,
    friendOption: WriteOptionsType2.FRIENDS,
    friendsList: List<WriteFriend>,
    optionClicked: (WriteOptionsType2.FRIENDS.EnableType) -> Unit
) {
    val color =
        if (friendOption.enableType == WriteOptionsType2.FRIENDS.EnableType.Enable) Gray10 else Gray6

    val isNeedToWave = friendOption.enableType == WriteOptionsType2.FRIENDS.EnableType.NoWaverPlus

    val isValid = friendOption.enableType != WriteOptionsType2.FRIENDS.EnableType.Disable

    val padding = if (isNeedToWave) 0.dp else 18.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(isValid) { optionClicked(friendOption.enableType) }
    ) {
        Divider(color = Gray4)

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 28.dp)
                .padding(top = padding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyText(
                text = stringResource(R.string.optionFriendAddTitle),
                modifier = Modifier,
                color = color,
                fontSize = dpToSp(16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isNeedToWave) {
                Image(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .padding(vertical = 14.dp),
                    painter = painterResource(R.drawable.chip2),
                    contentDescription = null
                )
            }

            Image(
                modifier = Modifier
                    .padding(end = 20.dp)
                    .sizeIn(16.dp),
                painter = painterResource(CommonR.drawable.ico_16_right),
                colorFilter = ColorFilter.tint(color),
                contentDescription = null
            )

        }

        if (friendsList.isNotEmpty()) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 18.dp, start = 28.dp, end = 56.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                friendsList.forEach { friend ->
                    MyText(
                        text = "@${friend.nickname}",
                        color = Main3,
                        fontSize = dpToSp(16.dp)
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.padding(bottom = padding))
        }
    }
}

@Composable
fun OpenTypeOptionView(
    modifier: Modifier,
    openType: WriteOpenType,
    optionClicked: () -> Unit
) {
    val typeText = when (openType) {
        WriteOpenType.PUBLIC -> stringResource(R.string.optionOpenTypePublic)
        WriteOpenType.PRIVATE -> stringResource(R.string.optionOpenTypePrivate)
        WriteOpenType.FRIENDS_OPEN -> stringResource(R.string.optionOpenTypeFriends)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { optionClicked() }
    ) {
        Divider(color = Gray4)
        Divider(color = Gray2, thickness = 7.dp)
        Divider(color = Gray3)

        OptionTextView(
            modifier = Modifier.padding(top = 18.dp),
            text = stringResource(R.string.optionOpenTypeTitle)
        )

        MyText(
            text = typeText,
            modifier = Modifier.padding(top = 12.dp, bottom = 18.dp, start = 28.dp, end = 28.dp),
            color = Main3,
            fontSize = dpToSp(16.dp)
        )
    }
}

@Composable
private fun OptionTextView(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = Gray10,
    arrowColor: Color = Gray10
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyText(
            text = text,
            modifier = Modifier,
            color = textColor,
            fontSize = dpToSp(16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier
                .padding(end = 20.dp)
                .sizeIn(16.dp),
            painter = painterResource(CommonR.drawable.ico_16_right),
            colorFilter = ColorFilter.tint(arrowColor),
            contentDescription = null
        )
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WriteScrapOptionView(
    modifier: Modifier,
    isScrapAvailable: Boolean,
    isScrapUsed: Boolean,
    scrapChanged: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val tooltipPosition = TooltipDefaults.rememberRichTooltipPositionProvider((-100).dp)
    val tooltipState = rememberBasicTooltipState(isPersistent = true)
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {

        Divider(color = Gray3)

        Row(
            modifier = Modifier
                .padding(start = 28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTooltipBox(
                positionProvider = tooltipPosition,
                tooltip = {
                    HtmlText2(
                        html = stringResource(R.string.optionScrapInfoDescGuide),
                        fontSize = 14.dp,
                        modifier = Modifier
                            .background(
                                color = Gray1,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .border(width = 1.dp, color = Gray3, shape = RoundedCornerShape(4.dp))
                            .padding(16.dp)
                    )
                },
                state = tooltipState
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyText(
                        text = stringResource(id = R.string.optionScrap),
                        color = if (isScrapAvailable) Gray10 else Gray7,
                        fontSize = dpToSp(16.dp)
                    )
                    IconButton(
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .size(16.dp),
                        onClick = {
                            coroutineScope.launch {
                                tooltipState.show()
                            }
                        },
                        image = R.drawable.btn_16_info,
                        contentDescription = stringResource(id = R.string.optionScrapInfoDesc)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (isScrapAvailable) {
                Switch(
                    modifier = Modifier
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
                        .padding(vertical = 12.5.dp)
                        .padding(end = 20.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    Toast.makeText(
                                        context,
                                        R.string.optionScrapNotAvailable,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                )
            }
        }

        Divider(color = Gray4)
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

@Preview
@Composable
private fun SelectOpenTypePopupPreview() {
    SelectOpenTypePopup(true, {}) {

    }
}

@Preview(showBackground = true)
@Composable
private fun OptionTextViewPreview() {
    OptionTextView(text = "테스트")
}

@Preview(showBackground = true)
@Composable
private fun KeywordsOptionPreview() {
    KeywordsOptionView(
        modifier = Modifier, keywordList = listOf(
            WriteKeyWord("1", "키워드1"),
            WriteKeyWord("2", "키워드2")
        )
    ) {}
}

@Preview(showBackground = true)
@Composable
private fun FriendsOptionPreview() {
    Column {
        FriendsOptionView(
            modifier = Modifier, friendsList = listOf(
                WriteFriend(id = "1", imageUrl = "", nickname = "이름냠"),
                WriteFriend(id = "1", imageUrl = "", nickname = "이름냠"),
                WriteFriend(id = "1", imageUrl = "", nickname = "이름냠"),
                WriteFriend(id = "1", imageUrl = "", nickname = "이름냠"),
                WriteFriend(id = "1", imageUrl = "", nickname = "이름냠")
            ), friendOption = WriteOptionsType2.FRIENDS(WriteOptionsType2.FRIENDS.EnableType.Enable)
        ) {}

        FriendsOptionView(
            modifier = Modifier,
            friendsList = listOf(),
            friendOption = WriteOptionsType2.FRIENDS(WriteOptionsType2.FRIENDS.EnableType.NoWaverPlus)
        ) {}

        FriendsOptionView(
            modifier = Modifier,
            friendsList = listOf(),
            friendOption = WriteOptionsType2.FRIENDS(WriteOptionsType2.FRIENDS.EnableType.Disable)
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun WriteScrapOptionPreview() {
    Column {
        WriteScrapOptionView(
            modifier = Modifier,
            isScrapAvailable = true,
            isScrapUsed = true,
            scrapChanged = {}
        )
        WriteScrapOptionView(
            modifier = Modifier,
            isScrapAvailable = false,
            isScrapUsed = false,
            scrapChanged = {}
        )
    }
}

@Preview
@Composable
private fun OPenTypeOptionPreview() {
    OpenTypeOptionView(modifier = Modifier, openType = WriteOpenType.PUBLIC, optionClicked = {})
}
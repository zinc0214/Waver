package com.zinc.waver.ui.presentation.component.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Error2
import com.zinc.waver.ui.design.theme.Gray5
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.MyTextField
import com.zinc.waver.ui.presentation.model.ProfileEditData
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui.util.isValidNicknameCheck
import com.zinc.waver.ui_common.R

@Composable
fun ProfileEditView(
    editData: ProfileEditData,
    needLengthCheck: Boolean,
    dataChanged: (String) -> Unit,
    isAlreadyUsedName: Boolean
) {

    val editDataState = remember { mutableStateOf(editData) }
    var currentText by remember { mutableStateOf(editDataState.value.prevText) }
    var currentTextSize by remember { mutableStateOf(currentText.length) }
    val isAlreadyUsedNameState = remember { mutableStateOf(isAlreadyUsedName) }
    var isValidNickname by remember { mutableStateOf(false) }
    val isNickNameType = editDataState.value.dataType == ProfileEditData.ProfileDataType.NICKNAME
    val titleText = if (isNickNameType) {
        stringResource(id = R.string.profileSettingNickNameTitle)
    } else {
        stringResource(id = R.string.profileSettingBioTitle)
    }

    LaunchedEffect(key1 = editData, block = {
        editDataState.value = editData
    })

    LaunchedEffect(currentText) {
        isValidNickname = isValidNicknameCheck(currentText)
    }
    LaunchedEffect(key1 = isAlreadyUsedName) {
        isAlreadyUsedNameState.value = isAlreadyUsedName
    }

    val maxLength = 30
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .padding(horizontal = 28.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

            MyText(
                text = titleText,
                color = Gray6,
                fontSize = dpToSp(dp = 14.dp)
            )

            if (needLengthCheck) {
                Spacer(modifier = Modifier.weight(1f))
                MyText(
                    text = "$currentTextSize/$maxLength",
                    color = Gray5,
                    fontSize = dpToSp(dp = 12.dp)
                )
            }
        }

        MyTextField(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            value = currentText,
            singleLine = isNickNameType,
            textStyle = TextStyle(fontSize = dpToSp(dp = 20.dp)),
            onValueChange = { changeText ->
                currentText = if (changeText.length > 30) {
                    val lastIndex = changeText.lastIndex
                    changeText.removeRange(maxLength - 1, lastIndex)
                } else {
                    changeText
                }
                currentTextSize = currentText.length
                dataChanged(currentText)
            })

        Divider(
            modifier = Modifier.padding(top = 15.5.dp),
            color = if (isAlreadyUsedNameState.value) Error2 else Gray5,
            thickness = 1.dp
        )

        if (isAlreadyUsedNameState.value) {
            MyText(
                modifier = Modifier.padding(top = 7.5.dp),
                text = stringResource(id = R.string.alreadyUsedNickNameGuide),
                color = Error2,
                fontSize = dpToSp(dp = 12.dp)
            )
        }

        if (isNickNameType) {
            if (currentTextSize < 3 || !isValidNickname) {
                val text = R.string.enterCharacter
                MyText(
                    modifier = Modifier.padding(top = 7.5.dp),
                    text = stringResource(id = text),
                    color = Gray6,
                    fontSize = dpToSp(dp = 12.dp)
                )
            }
        }

    }
}
package com.zinc.waver.ui.presentation.login.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.zinc.waver.ui.presentation.component.TitleIconType
import com.zinc.waver.ui.presentation.component.TitleView

@Composable
internal fun ProfileCreateTitle(
    title: String
) {
    TitleView(
        title = title,
        leftIconType = TitleIconType.BACK,
        isDividerVisible = true
    )
}

@Preview
@Composable
private fun ProfileCreateTitlePreview() {
    ProfileCreateTitle(title = "프로필 작성")
}
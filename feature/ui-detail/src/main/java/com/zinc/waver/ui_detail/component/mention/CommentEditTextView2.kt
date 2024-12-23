package com.zinc.waver.ui_detail.component.mention

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.zinc.waver.ui_detail.model.OpenDetailEditTextViewEvent
import com.zinc.waver.ui_detail.model.TaggedTextInfo

@Composable
fun CommentEditTextView2(
    originText: String,
    isLiked: Boolean,
    newTaggedInfo: TaggedTextInfo? = null,
    commentEvent: (OpenDetailEditTextViewEvent) -> Unit
) {
    AndroidView(modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            CommentEditTextAndroidView(
                context,
                originText = originText,
                isLiked = isLiked,
                commentEvent = commentEvent
            )
        }, update = {
            it.updateText(originText, newTaggedInfo = newTaggedInfo)
        })
}
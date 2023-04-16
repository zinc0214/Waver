package com.zinc.berrybucket.ui.presentation.detail.component.mention

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.zinc.berrybucket.ui.presentation.detail.model.OpenDetailEditTextViewEvent

@Composable
fun CommentEditTextView2(
    originText: String,
    commentEvent: (OpenDetailEditTextViewEvent) -> Unit
) {
    Log.e("ayhan", "TextView2 originText1  :$originText")
    val updatedText = remember { mutableStateOf(originText) }

    Log.e("ayhan", "TextView2 originText  :${updatedText.value}")


    AndroidView(modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            CommentEditTextAndroidView(context).apply {
                setData(
                    originText = originText,
                    commentEvent = commentEvent
                )
            }
        }, update = {
            it.updateText(originText)
        })
}
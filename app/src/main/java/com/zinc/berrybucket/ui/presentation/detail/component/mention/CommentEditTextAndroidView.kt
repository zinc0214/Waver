package com.zinc.berrybucket.ui.presentation.detail.component.mention

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.zinc.berrybucket.databinding.LayoutMentionTagViewBinding
import com.zinc.berrybucket.ui.presentation.detail.model.OpenDetailEditTextViewEvent

class CommentEditTextAndroidView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: LayoutMentionTagViewBinding
    private var commentEvent: ((OpenDetailEditTextViewEvent) -> Unit)? = null
    private var originText: String = ""
    private var beforeText: String = originText

    init {
        setUpViews()
    }

    private fun setUpViews() {

        binding = LayoutMentionTagViewBinding.inflate(LayoutInflater.from(context), this, true)

        binding.commentEditTextView.setText(originText)
        binding.commentEditTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                // 커서 위치 조정
                // 값이 커진 경우 start, 적어진 경우 start - 1
                val startIndex = if (beforeText.lastIndex <= (s?.lastIndex ?: 0)) {
                    start
                } else start - 1

                Log.e("ayhan", "onTextChanged : $start $before $count $startIndex $beforeText ${s}")

                commentEvent?.invoke(
                    OpenDetailEditTextViewEvent.TextChanged(
                        s.toString(),
                        startIndex
                    )
                )
                beforeText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    fun setData(
        originText: String,
        commentEvent: (OpenDetailEditTextViewEvent) -> Unit
    ) {
        Log.e("ayhan", "originText : $originText")
        this.originText = originText
        this.commentEvent = commentEvent
    }

    fun updateText(
        originText: String,
    ) {
        Log.e("ayhan", "originText Sc : $originText")

        if (binding.commentEditTextView.text.toString() != originText) {
            binding.commentEditTextView.setText(originText)
            binding.commentEditTextView.setSelection(originText.lastIndex)
        }

    }
}
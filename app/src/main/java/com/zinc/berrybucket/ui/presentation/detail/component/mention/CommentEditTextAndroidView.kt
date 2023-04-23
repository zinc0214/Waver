package com.zinc.berrybucket.ui.presentation.detail.component.mention

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.zinc.berrybucket.databinding.LayoutMentionTagViewBinding
import com.zinc.berrybucket.ui.presentation.detail.model.OpenDetailEditTextViewEvent
import com.zinc.berrybucket.ui.presentation.detail.model.TaggedTextInfo

class CommentEditTextAndroidView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: LayoutMentionTagViewBinding
    private var commentEvent: ((OpenDetailEditTextViewEvent) -> Unit)? = null
    private var originText: String = ""
    private var beforeText: String = originText
    private var _spannableString = mutableMapOf<String, TaggedTextInfo>()
    private var changeStartIndex = 0

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
                Log.e("ayhan", "onTextChanged : $start $before $count $beforeText ${s}")

                if (s.toString() != beforeText) {

                    changeStartIndex = start

                    val taggedString = _spannableString.values.firstOrNull {
                        it.startIndex > changeStartIndex || changeStartIndex <= it.endIndex
                    }

                    Log.e("ayhan", "remove taggedString  :$taggedString")
                    if (taggedString != null) {
                        if (s.toString().length > beforeText.length) {
                            // _spannableString.remove(taggedString)
                            binding.commentEditTextView.setText(beforeText)
                            binding.commentEditTextView.setSelection(taggedString.endIndex)
                            updateText(getMakeTaggedText(beforeText), isForceUpdate = true)
                            Log.e("ayhan", "taggedStringRemoved ")
                        } else if (s.toString().length < beforeText.length) {
                            _spannableString.values.remove(taggedString)
                            val removeText = beforeText.removeRange(
                                taggedString.startIndex,
                                taggedString.endIndex + 1
                            )
                            Log.e(
                                "ayhan",
                                "taggedStringRemoved _spannableString : $_spannableString"
                            )
                            updateText(getMakeTaggedText(removeText), isForceUpdate = true)
                        }
                    } else {
                        beforeText = s.toString()
                    }

                    Log.e("ayhan", "beforeText1  :$beforeText")
                }
            }

            override fun afterTextChanged(s: Editable?) {


                // 만약 현재 수정한 값이 태그 위치인 경우, 태그를 제거한다.
                Log.e("ayhan", "after s :$s,$changeStartIndex,${_spannableString},$beforeText")


                // 커서 위치 조정
                // 값이 커진 경우 start, 적어진 경우 start - 1
                val startIndex = if (beforeText.lastIndex <= (s?.lastIndex ?: 0)) {
                    changeStartIndex
                } else changeStartIndex - 1

                commentEvent?.invoke(
                    OpenDetailEditTextViewEvent.TextChanged(
                        beforeText,
                        startIndex + 1,
                        _spannableString.values.toList()
                    )
                )
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

    private fun getMakeTaggedText(originText: String): String {
        var makeTaggedText = originText

        _spannableString.values.forEachIndexed { index, taggedTextInfo ->
            Log.e("ayhan", "getMakeTaggedText taggedTextInfo : $taggedTextInfo")

            // 첫번째 아이템이 아닌 경우, 위치가 변경되었을 거라서 값을 추가해야함
            var sliceIndexStart = taggedTextInfo.startIndex
            var sliceIndexEnd = taggedTextInfo.endIndex

            if (index > 0) {
                sliceIndexStart += index + 1
                sliceIndexEnd += index + 1
            }

            val originSliceText = makeTaggedText.slice(sliceIndexStart..sliceIndexEnd)

            Log.e("ayhan", "getMakeTaggedText originSliceText : $originSliceText")

            if (originSliceText == taggedTextInfo.text) {
                makeTaggedText =
                    makeTaggedText.replaceRange(
                        sliceIndexStart..sliceIndexEnd,
                        "|${taggedTextInfo.text}`"
                    )
                Log.e("ayhan", "getMakeTaggedText makeTaggedText : $makeTaggedText")
            }
        }

        return makeTaggedText
    }

    fun updateText(
        originText: String,
        newTaggedInfo: TaggedTextInfo? = null,
        isForceUpdate: Boolean = false
    ) {
        Log.e("ayhan", "originText Sc : $originText")

        if (newTaggedInfo != null) {
            _spannableString[newTaggedInfo.id] = newTaggedInfo
        }

        val reformatText = getMakeTaggedText(originText)

        Log.e("ayhan", "reformatText Sc : $reformatText")

        if (binding.commentEditTextView.text.toString() != originText || isForceUpdate) {

            val startIndexList = mutableListOf<Int>() // 시작 인덱스를 저장할 리스트
            val endIndexList = mutableListOf<Int>() // 끝 인덱스를 저장할 리스트

            var startIndex = reformatText.indexOf("|") // 시작 인덱스 초기값 설정
            while (startIndex != -1) { // 모든 시작 인덱스를 찾을 때까지 반복
                startIndexList.add(startIndex)
                startIndex = reformatText.indexOf("|", startIndex + 1) // 다음 시작 인덱스 찾기
            }

            for (startIndex in startIndexList) {
                val endIndex = reformatText.indexOf("`", startIndex + 1) // 끝 인덱스 찾기
                endIndexList.add(endIndex)
            }

            val spannableString = SpannableStringBuilder(reformatText)
            for (i in 0 until startIndexList.size) {
                val start = startIndexList[i] // @부터 시작
                val end = endIndexList[i] // 태그된 곳 까지만
                Log.e("ayhan", "index ::: $start, $end")
                spannableString.setSpan(
                    ForegroundColorSpan(Color.RED), // 변경할 색상 설정
                    start, // 시작 인덱스 설정
                    end, // 끝 인덱스 설정 (+2는 끝 인덱스를 inclusive로 포함하기 위함)
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE // 스팬 적용 범위 설정
                )

                val sliceStartIndex = start + 1
                val sliceEndIndex = end - 1
                val taggedText = reformatText.slice(sliceStartIndex..sliceEndIndex)
                Log.e("ayhan", "taggedText  ;$taggedText, ${taggedText.length}")
            }

            Log.e("ayhan", "_spannableString  ;$_spannableString")

            // "@" 문자열을 빈 문자열로 바꿉니다.
            var index = spannableString.indexOf("|")
            while (index >= 0) {
                spannableString.replace(index, index + 1, "")
                index = spannableString.indexOf("|")
            }

            // "!" 문자열을 빈 문자열로 바꿉니다.
            index = spannableString.indexOf("`")
            while (index >= 0) {
                spannableString.replace(index, index + 1, "")
                index = spannableString.indexOf("`")
            }

            val resultText =
                spannableString.toString() // 제거된 텍스트로 업데이트된 SpannableStringBuilder를 다시 String으로 변환

            Log.e("ayhan", "resultText :$resultText")

            beforeText = spannableString.toString()
            changeStartIndex = resultText.length
            binding.commentEditTextView.text = spannableString
            binding.commentEditTextView.setSelection(resultText.length)

        }

    }
}
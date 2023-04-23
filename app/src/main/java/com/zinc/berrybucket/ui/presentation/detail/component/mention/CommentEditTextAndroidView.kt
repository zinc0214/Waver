package com.zinc.berrybucket.ui.presentation.detail.component.mention

import android.content.Context
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.zinc.berrybucket.R
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
                        changeStartIndex in it.startIndex..it.endIndex
                    }

                    Log.e("ayhan", "remove taggedString  :$taggedString")
                    if (taggedString != null) {
                        if (s.toString().length > beforeText.length) {
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
                            updateTaggedString(taggedString, false)
                            Log.e(
                                "ayhan",
                                "taggedStringRemoved _spannableString : $_spannableString"
                            )
                            updateText(getMakeTaggedText(removeText), isForceUpdate = true)
                        }
                    } else {
                        updateTaggedString(s.toString())
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

    // 태그된 텍스트 사이로 다른 텍스트가 추가되었을 때, 그 텍스트 만큼 기존의 index 를 업데이트 하기 위함
    private fun updateTaggedString(changeText: String) {
        Log.e("ayhan", "updateTaggedString")
        var size = 0

        // 현재 수정된 Index 를 기반으로 앞쪽에서 가장 가까운 tag text
        val lastTagged = _spannableString.values.lastOrNull { it.endIndex < changeStartIndex }
        // 현재 수정된 Index 를 기반으로 뒤에서 가장 가까운 tag text
        val firstTagged = _spannableString.values.firstOrNull { it.startIndex > changeStartIndex }

        Log.e("ayhan", "lastTagged : $lastTagged, firstTagged $firstTagged")

        // 태그 사이에 낀 텍스트인 경우
        if (lastTagged != null && firstTagged != null) {

            // 값이 추가된 경우
            if (changeText.length > beforeText.length) {
                // 변경된 텍스트
                val betweenText = changeText.slice(lastTagged.endIndex + 1..firstTagged.startIndex)

                // 기존 텍스트
                val currentText =
                    beforeText.slice(lastTagged.endIndex + 1 until firstTagged.startIndex)
                Log.e("ayhan", "betweenText1 :$betweenText,$currentText")

                size = betweenText.length - currentText.length
            }
            // 값이 삭제된 경우
            else if (changeText.length < beforeText.length) {
                // 변경된 텍스트
                val betweenText =
                    changeText.slice(lastTagged.endIndex + 1 until firstTagged.startIndex - 1)

                // 기존 텍스트
                val currentText =
                    beforeText.slice(lastTagged.endIndex + 1 until firstTagged.startIndex)
                Log.e("ayhan", "betweenText2 :$betweenText,$currentText")

                size = betweenText.length - currentText.length
            }

        }

        // 모든 태그 전에 수정된 텍스트인 경우
        else if (firstTagged != null) {
            // 값이 추가된 경우
            if (changeText.length > beforeText.length) {
                // 변경된 텍스트
                val betweenText = changeText.slice(0..firstTagged.startIndex)

                // 기존 텍스트
                val currentText =
                    beforeText.slice(0 until firstTagged.startIndex)
                Log.e("ayhan", "betweenText3 :$betweenText,$currentText")

                size = betweenText.length - currentText.length
            }
            // 값이 삭제된 경우
            else if (changeText.length < beforeText.length) {
                // 변경된 텍스트
                val betweenText =
                    changeText.slice(0 until firstTagged.startIndex)

                // 기존 텍스트
                val currentText =
                    beforeText.slice(0..firstTagged.startIndex)
                Log.e("ayhan", "betweenText4 :$betweenText,$currentText")

                size = betweenText.length - currentText.length
            }
        }

        _spannableString.values.filter { it.startIndex > changeStartIndex }.map {
            it.copy(startIndex = it.startIndex + size, endIndex = it.endIndex + size)
        }.forEach { modifiedTaggedTextInfo ->
            val id = modifiedTaggedTextInfo.id
            _spannableString[id] = modifiedTaggedTextInfo

            Log.e("ayhan", "modifiedTaggedTextInfo : $_spannableString")
        }
    }

    // 앞의 태그가 지워진 경우, 뒤에 있는 태그의 index 값을 수정
    private fun updateTaggedString(changedItem: TaggedTextInfo, isAdd: Boolean) {
        val size = if (isAdd) changedItem.text.length else -(changedItem.text.length)

        if (isAdd) {
            // 추가된 경우
            _spannableString.values.filter { it.startIndex >= changedItem.startIndex }.map {
                it.copy(
                    startIndex = it.startIndex + changedItem.endIndex,
                    endIndex = it.endIndex + changedItem.endIndex
                )
            }.forEach { modifiedTaggedTextInfo ->
                val id = modifiedTaggedTextInfo.id
                _spannableString[id] = modifiedTaggedTextInfo

                Log.e("ayhan", "modifiedTaggedTextInfo2 : $_spannableString")
            }
        } else {
            // 삭제된 경우
            _spannableString.values.filter { it.startIndex > changedItem.endIndex }.map {
                it.copy(
                    startIndex = it.startIndex + size,
                    endIndex = it.endIndex + size
                )
            }.forEach { modifiedTaggedTextInfo ->
                val id = modifiedTaggedTextInfo.id
                _spannableString[id] = modifiedTaggedTextInfo

                Log.e("ayhan", "modifiedTaggedTextInfo3 : $_spannableString")
            }
        }
    }

    // 태그된 텍스트에 컬러를 입히기 위해 강제로 앞뒤에 특수한 문자열을 넣음
    private fun getMakeTaggedText(originText: String): String {
        var makeTaggedText = originText
        val sortedList = _spannableString.values.sortedBy { it.startIndex }
        Log.e("ayhan", "sortedList : $sortedList")

        sortedList.forEachIndexed { index, taggedTextInfo ->
            Log.e("ayhan", "getMakeTaggedText taggedTextInfo : $taggedTextInfo")

            // 첫번째 아이템이 아닌 경우, 위치가 변경되었을 거라서 값을 추가해야함
            var sliceIndexStart = taggedTextInfo.startIndex
            var sliceIndexEnd = taggedTextInfo.endIndex

            if (index > 0) {
                // 기존 index 값에 계속해서 추가된 텍스트를 더해줌. ("|" 와 "`")
                sliceIndexStart += index + index
                sliceIndexEnd += index + index
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
            updateTaggedString(newTaggedInfo, true)
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
                    ForegroundColorSpan(context.getColor(R.color.main5)), // 변경할 색상 설정
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